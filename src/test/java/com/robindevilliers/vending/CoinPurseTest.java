package com.robindevilliers.vending;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CoinPurseTest {

    @Test
    public void testAddCoin_givenOneCoin() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");

        Map<String, Integer> counts = coinPurse.getCounts();
        assertThat(counts.get("DOLLAR"), is(1));
    }

    @Test
    public void testAddCoin_givenTwoCoins() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DOLLAR");

        Map<String, Integer> counts = coinPurse.getCounts();
        assertThat(counts.get("DOLLAR"), is(2));
    }

    @Test
    public void testAddCoin_givenSomeCoins() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DIME");

        Map<String, Integer> counts = coinPurse.getCounts();
        assertThat(counts.get("DOLLAR"), is(2));
        assertThat(counts.get("DIME"), is(1));
    }

    @Test
    public void testRefund_givenOneCoin() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");

        List<String> responses = new ArrayList<>();
        coinPurse.refund(responses);

        assertThat(responses.size(), is(1));
        assertThat(responses.get(0), is("DOLLAR"));
    }

    @Test
    public void testRefund_givenTwoCoins() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DIME");

        List<String> responses = new ArrayList<>();
        coinPurse.refund(responses);

        assertThat(responses.size(), is(2));
        assertThat(responses.get(0), is("DOLLAR"));
        assertThat(responses.get(1), is("DIME"));
    }

    @Test
    public void testGetCounts_givenOneCoin() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");

        Map<String, Integer> counts = coinPurse.getCounts();

        assertThat(counts.containsKey("DOLLAR"), is(true));
        assertThat(counts.get("DOLLAR"), is(1));
    }

    @Test
    public void testGetCounts_givenTwoOfTheSameCoins() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DOLLAR");

        Map<String, Integer> counts = coinPurse.getCounts();

        assertThat(counts.containsKey("DOLLAR"), is(true));
        assertThat(counts.get("DOLLAR"), is(2));
    }

    @Test
    public void testGetCounts_givenTwoDifferentCoins() {

        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DIME");

        Map<String, Integer> counts = coinPurse.getCounts();

        assertThat(counts.containsKey("DOLLAR"), is(true));
        assertThat(counts.get("DOLLAR"), is(1));

        assertThat(counts.containsKey("DIME"), is(true));
        assertThat(counts.get("DIME"), is(1));
    }

    @Test
    public void testDropCoinsIntoSafe() {
        CoinPurse coinPurse = new CoinPurse();

        coinPurse.addCoin("DOLLAR");
        coinPurse.addCoin("DIME");

        coinPurse.dropPaymentIntoSafe();

        Map<String, Integer> counts = coinPurse.getCounts();

        assertThat(counts.containsKey("DOLLAR"), is(false));
        assertThat(counts.containsKey("DIME"), is(false));
    }
}
