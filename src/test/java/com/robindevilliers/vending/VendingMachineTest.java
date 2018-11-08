package com.robindevilliers.vending;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    @Mock
    CoinCalculator coinCalculator;

    @Mock
    Cassettes cassettes;

    @Mock
    ItemBuckets itemBuckets;

    @Mock
    CoinPurse coinPurse;

    @InjectMocks
    VendingMachine vendingMachine;

    @After
    public void after() {
        verifyNoMoreInteractions(coinCalculator, cassettes, itemBuckets, coinPurse);
    }

    @Test
    public void testServiceCommand() {

        String response = vendingMachine.process("SERVICE");

        assertThat(response, is("SERVICE-OK"));

        verify(itemBuckets).reset();

        verify(cassettes).reset();
    }

    @Test
    public void testPutCommand_givenValidCoin() {

        when(coinCalculator.hasDenomination("NICKEL")).thenReturn(true);

        String response = vendingMachine.process("PUT-NICKEL");

        assertThat(response, is(""));

        verify(coinCalculator).hasDenomination("NICKEL");
        verify(coinPurse).addCoin("NICKEL");
    }

    @Test
    public void testPutCommand_givenInvalidCoin() {

        when(coinCalculator.hasDenomination("NICKEL")).thenReturn(false);

        String response = vendingMachine.process("PUT-NICKEL");

        assertThat(response, is("INVALID-COIN"));

        verify(coinCalculator).hasDenomination("NICKEL");
    }


    @Test
    public void testCoinReturnCommand() {
        String response = vendingMachine.process("COIN-RETURN");

        assertThat(response, is(""));

        verify(coinPurse).refund(emptyList());
    }


    @Test
    public void testGetItemCommand_givenInvalidItemCode() {

        when(itemBuckets.isValidItemId("A")).thenReturn(false);

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("INVALID-ITEM"));

        verify(itemBuckets).isValidItemId("A");
    }

    @Test
    public void testGetItemCommand_givenValidItem_andOutOfStock() {

        when(itemBuckets.isValidItemId("A")).thenReturn(true);
        when(itemBuckets.hasItem("A")).thenReturn(false);

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("OUT-OF-STOCK"));

        verify(itemBuckets).isValidItemId("A");
        verify(itemBuckets).hasItem("A");
    }

    @Test
    public void testGetItemCommand_givenItemInStock_andInsufficientFunds() {

        when(itemBuckets.isValidItemId("A")).thenReturn(true);
        when(itemBuckets.hasItem("A")).thenReturn(true);
        when(itemBuckets.getPriceForItem("A")).thenReturn(65);
        when(coinCalculator.calculateTotal(this.coinPurse)).thenReturn(55);

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("INSUFFICIENT-FUNDS"));

        verify(itemBuckets).isValidItemId("A");
        verify(itemBuckets).hasItem("A");
        verify(itemBuckets).getPriceForItem("A");
        verify(coinCalculator).calculateTotal(this.coinPurse);
    }

    @Test
    public void testGetItemCommand_givenItemInStock_andInsufficientChange() throws InsufficientChangeException {

        when(itemBuckets.isValidItemId("A")).thenReturn(true);
        when(itemBuckets.hasItem("A")).thenReturn(true);
        when(itemBuckets.getPriceForItem("A")).thenReturn(65);
        when(coinCalculator.calculateTotal(this.coinPurse)).thenReturn(100);
        when(coinCalculator.calculateChange(35, this.cassettes)).thenThrow(InsufficientChangeException.class);

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("INSUFFICIENT-CHANGE"));

        verify(itemBuckets).isValidItemId("A");
        verify(itemBuckets).hasItem("A");
        verify(itemBuckets).getPriceForItem("A");
        verify(coinCalculator).calculateTotal(this.coinPurse);
        verify(coinCalculator).calculateChange(35, this.cassettes);
    }

    @Test
    public void testGetItemCommand_givenItemInStock_andExactFunds() throws InsufficientChangeException {

        when(itemBuckets.isValidItemId("A")).thenReturn(true);
        when(itemBuckets.hasItem("A")).thenReturn(true);
        when(itemBuckets.getPriceForItem("A")).thenReturn(65);
        when(coinCalculator.calculateTotal(this.coinPurse)).thenReturn(65);
        when(coinCalculator.calculateChange(0, this.cassettes)).thenReturn(emptyList());

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("A"));

        verify(itemBuckets).isValidItemId("A");
        verify(itemBuckets).hasItem("A");
        verify(itemBuckets).getPriceForItem("A");
        verify(coinCalculator).calculateTotal(any());
        verify(coinCalculator).calculateChange(0, this.cassettes);

        verify(coinPurse).dropPaymentIntoSafe();
        verify(itemBuckets).ejectItem("A");
    }

    @Test
    public void testGetItemCommand_givenItemInStock_andChange() throws InsufficientChangeException {

        when(itemBuckets.isValidItemId("A")).thenReturn(true);
        when(itemBuckets.hasItem("A")).thenReturn(true);
        when(itemBuckets.getPriceForItem("A")).thenReturn(65);
        when(coinCalculator.calculateTotal(this.coinPurse)).thenReturn(100);
        when(coinCalculator.calculateChange(35, this.cassettes)).thenReturn(asList("QUARTER","DIME"));

        String response = vendingMachine.process("GET-A");

        assertThat(response, is("A, QUARTER, DIME"));

        verify(itemBuckets).isValidItemId("A");
        verify(itemBuckets).hasItem("A");
        verify(itemBuckets).getPriceForItem("A");
        verify(coinCalculator).calculateTotal(any());
        verify(coinCalculator).calculateChange(35, this.cassettes);

        verify(coinPurse).dropPaymentIntoSafe();
        verify(itemBuckets).ejectItem("A");
    }

    @Test
    public void testInvalidCommand(){
        String response = vendingMachine.process("A");

        assertThat(response, is("INVALID-COMMAND"));
    }


}
