package com.robindevilliers.vending;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.robindevilliers.vending.model.Denomination;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinCalculatorTest {

    @Test
    public void testHasDenominations_givenDenominationPresent() {


        boolean result = new CoinCalculator(singletonList(new Denomination("DOLLAR", 100))).hasDenomination("DOLLAR");

        assertThat(result, is(true));
    }

    @Test
    public void testHasDenominations_givenDenominationAbsent() {
        Map<String, Integer> denominations = new HashMap<>();

        boolean result = new CoinCalculator(Collections.emptyList()).hasDenomination("DOLLAR");

        assertThat(result, is(false));
    }


    @Test
    public void testCalculateTotal_givenOneDollar() {

        CoinPurse coinPurse = new CoinPurse();
        coinPurse.addCoin("DOLLAR");

        int total = new CoinCalculator(singletonList(new Denomination("DOLLAR", 100))).calculateTotal(coinPurse);

        assertThat(total, is(100));
    }

    @Test
    public void testCalculateTotal_givenTwoDollars() {

        CoinPurse coinPurse = new CoinPurse();
        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DOLLAR");

        int total = new CoinCalculator(singletonList(new Denomination("DOLLAR", 100))).calculateTotal(coinPurse);

        assertThat(total, is(200));
    }

    @Test
    public void testCalculateChange_happyScenario440() throws InsufficientChangeException {
        List<Denomination> denominations = new ArrayList<>();

        denominations.add(new Denomination("DIME", 10));
        denominations.add(new Denomination("QUARTER", 25));
        denominations.add(new Denomination("NICKEL", 5));
        denominations.add(new Denomination("DOLLAR", 100));

        Cassettes cassettes = mock(Cassettes.class);

        when(cassettes.getCoinCount("DOLLAR")).thenReturn(10);
        when(cassettes.getCoinCount("QUARTER")).thenReturn(10);
        when(cassettes.getCoinCount("NICKEL")).thenReturn(10);
        when(cassettes.getCoinCount("DIME")).thenReturn(10);

        List<String> coins = new CoinCalculator(denominations).calculateChange(440, cassettes);

        assertThat(coins.size(), is(7));
        assertThat(coins.get(0), is("DOLLAR"));
        assertThat(coins.get(1), is("DOLLAR"));
        assertThat(coins.get(2), is("DOLLAR"));
        assertThat(coins.get(3), is("DOLLAR"));
        assertThat(coins.get(4), is("QUARTER"));
        assertThat(coins.get(5), is("DIME"));
        assertThat(coins.get(6), is("NICKEL"));

        verify(cassettes).getCoinCount("DOLLAR");
        verify(cassettes).getCoinCount("QUARTER");
        verify(cassettes).getCoinCount("NICKEL");
        verify(cassettes).getCoinCount("DIME");

        verify(cassettes, times(4)).ejectCoin("DOLLAR");
        verify(cassettes).ejectCoin("QUARTER");
        verify(cassettes).ejectCoin("DIME");
        verify(cassettes).ejectCoin("NICKEL");

        verifyNoMoreInteractions(cassettes);
    }

    @Test
    public void testCalculateChange_happyScenario165() throws InsufficientChangeException {
        List<Denomination> denominations = new ArrayList<>();

        denominations.add(new Denomination("DIME", 10));
        denominations.add(new Denomination("QUARTER", 25));
        denominations.add(new Denomination("NICKEL", 5));
        denominations.add(new Denomination("DOLLAR", 100));

        Cassettes cassettes = mock(Cassettes.class);

        when(cassettes.getCoinCount("DOLLAR")).thenReturn(10);
        when(cassettes.getCoinCount("QUARTER")).thenReturn(10);
        when(cassettes.getCoinCount("NICKEL")).thenReturn(10);
        when(cassettes.getCoinCount("DIME")).thenReturn(10);

        List<String> coins = new CoinCalculator(denominations).calculateChange(165, cassettes);

        assertThat(coins.size(), is(5));
        assertThat(coins.get(0), is("DOLLAR"));
        assertThat(coins.get(1), is("QUARTER"));
        assertThat(coins.get(2), is("QUARTER"));
        assertThat(coins.get(3), is("DIME"));
        assertThat(coins.get(4), is("NICKEL"));
    }

    @Test
    public void testCalculateChange_insufficientDollars() throws InsufficientChangeException {
        List<Denomination> denominations = new ArrayList<>();

        denominations.add(new Denomination("DIME", 10));
        denominations.add(new Denomination("QUARTER", 25));
        denominations.add(new Denomination("NICKEL", 5));
        denominations.add(new Denomination("DOLLAR", 100));

        Cassettes cassettes = mock(Cassettes.class);

        when(cassettes.getCoinCount("DOLLAR")).thenReturn(0);
        when(cassettes.getCoinCount("QUARTER")).thenReturn(10);
        when(cassettes.getCoinCount("NICKEL")).thenReturn(10);
        when(cassettes.getCoinCount("DIME")).thenReturn(10);

        List<String> coins = new CoinCalculator(denominations).calculateChange(165, cassettes);

        assertThat(coins.size(), is(8));
        assertThat(coins.get(0), is("QUARTER"));
        assertThat(coins.get(1), is("QUARTER"));
        assertThat(coins.get(2), is("QUARTER"));
        assertThat(coins.get(3), is("QUARTER"));
        assertThat(coins.get(4), is("QUARTER"));
        assertThat(coins.get(5), is("QUARTER"));
        assertThat(coins.get(6), is("DIME"));
        assertThat(coins.get(7), is("NICKEL"));
    }

    @Test(expected = InsufficientChangeException.class)
    public void testCalculateChange_insufficentChange() throws InsufficientChangeException {
        List<Denomination> denominations = new ArrayList<>();

        denominations.add(new Denomination("DIME", 10));
        denominations.add(new Denomination("QUARTER", 25));
        denominations.add(new Denomination("NICKEL", 5));
        denominations.add(new Denomination("DOLLAR", 100));

        Cassettes cassettes = mock(Cassettes.class);

        new CoinCalculator(denominations).calculateChange(101, cassettes);
    }

}
