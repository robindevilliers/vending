package com.robindevilliers.vending;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.robindevilliers.vending.model.CoinCassette;
import com.robindevilliers.vending.model.Denomination;
import com.robindevilliers.vending.model.ItemBucket;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntegrationTest {

    @Test
    public void testScenario_exactChange() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(asList(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        )),
                new Cassettes(new ArrayList<>()),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-QUARTER, PUT-QUARTER, PUT-QUARTER, PUT-QUARTER, GET-B");

        assertThat(response, is("B"));
    }

    @Test
    public void testScenario_insufficientFunds() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(asList(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        )),
                new Cassettes(new ArrayList<>()),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-QUARTER, GET-B");

        assertThat(response, is("INSUFFICIENT-FUNDS"));
    }

    @Test
    public void testScenario_invalidItem() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(asList(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        )),
                new Cassettes(new ArrayList<>()),
                new CoinPurse()
        );

        String response = vendingMachine.process("GET-D");

        assertThat(response, is("INVALID-ITEM"));
    }

    @Test
    public void testScenario_invalidCoin() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(asList(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        )),
                new Cassettes(new ArrayList<>()),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-Q");

        assertThat(response, is("INVALID-COIN"));
    }

    @Test
    public void testScenario_coinReturn() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(asList(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        )),
                new Cassettes(new ArrayList<>()),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-QUARTER, PUT-QUARTER, COIN-RETURN");

        assertThat(response, is("QUARTER, QUARTER"));
    }

    @Test
    public void testScenario_buyWithChange() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(Collections.singletonList(new ItemBucket("A", 65, 10))),
                new Cassettes(asList(
                        new CoinCassette("DOLLAR", 100),
                        new CoinCassette("QUARTER", 100),
                        new CoinCassette("DIME", 100),
                        new CoinCassette("NICKEL", 100)
                )),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-DOLLAR, GET-A");

        assertThat(response, is("A, QUARTER, DIME"));
    }

    @Test
    public void testScenario_outOfStock() {

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(Collections.singletonList(new ItemBucket("A", 65, 0))),
                new Cassettes(asList(
                        new CoinCassette("DOLLAR", 100),
                        new CoinCassette("QUARTER", 100),
                        new CoinCassette("DIME", 100),
                        new CoinCassette("NICKEL", 100)
                )),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-DOLLAR, GET-A");

        assertThat(response, is("OUT-OF-STOCK"));
    }

    @Test
    public void testScenario_insufficientChange_andCoinReturnHasNotAffectedTheCoinCassettes() {

        Map<String, CoinCassette> coinReserve = Stream.of(
                new CoinCassette("DOLLAR", 0),
                new CoinCassette("QUARTER", 1),
                new CoinCassette("DIME", 0),
                new CoinCassette("NICKEL", 0)
        ).collect(Collectors.toMap(CoinCassette::getId, Function.identity()));

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(Collections.singletonList(new ItemBucket("A", 65, 10))),
                new Cassettes(new ArrayList<>(coinReserve.values())),
                new CoinPurse()
        );

        String response = vendingMachine.process("PUT-DOLLAR, GET-A, COIN-RETURN");

        assertThat(response, is("INSUFFICIENT-CHANGE, DOLLAR"));

        assertThat(coinReserve.get("DOLLAR").getQuantity(), is(0));
        assertThat(coinReserve.get("QUARTER").getQuantity(), is(1));
        assertThat(coinReserve.get("DIME").getQuantity(), is(0));
        assertThat(coinReserve.get("NICKEL").getQuantity(), is(0));
    }

    @Test
    public void testScenario_service() {

        Map<String, ItemBucket> itemStock = Stream.of(
                new ItemBucket("A", 65, 10),
                new ItemBucket("B", 100, 10),
                new ItemBucket("C", 150, 10)
        ).collect(Collectors.toMap(ItemBucket::getId, Function.identity()));

        Map<String, CoinCassette> coinReserve = Stream.of(
                new CoinCassette("DOLLAR", 0),
                new CoinCassette("QUARTER", 0),
                new CoinCassette("DIME", 0),
                new CoinCassette("NICKEL", 0)
        ).collect(Collectors.toMap(CoinCassette::getId, Function.identity()));

        VendingMachine vendingMachine = new VendingMachine(new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25),
                new Denomination("DIME", 10)
        )), new ItemBuckets(new ArrayList<>(itemStock.values())),
                new Cassettes(new ArrayList<>(coinReserve.values())),
                new CoinPurse()
        );

        String response = vendingMachine.process("SERVICE");

        assertThat(response, is("SERVICE-OK"));

        assertThat(coinReserve.get("DOLLAR").getQuantity(), is(100));
        assertThat(coinReserve.get("QUARTER").getQuantity(), is(100));
        assertThat(coinReserve.get("DIME").getQuantity(), is(100));
        assertThat(coinReserve.get("NICKEL").getQuantity(), is(100));

        assertThat(itemStock.get("A").getQuantity(), is(20));
        assertThat(itemStock.get("B").getQuantity(), is(20));
        assertThat(itemStock.get("C").getQuantity(), is(20));
    }
}
