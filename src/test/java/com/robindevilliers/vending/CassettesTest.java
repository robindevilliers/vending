package com.robindevilliers.vending;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.robindevilliers.vending.model.CoinCassette;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CassettesTest {

    @Test
    public void testAdd() {
        List<CoinCassette> cassetteList = new ArrayList<>();
        Cassettes cassettes = new Cassettes(cassetteList);

        cassettes.add(new CoinCassette("NICKEL", 5));

        assertThat(cassetteList.size(), is(1));
        assertThat(cassetteList.get(0).getId(), is("NICKEL"));
        assertThat(cassetteList.get(0).getQuantity(), is(5));
    }

    @Test
    public void testReset() {
        List<CoinCassette> cassetteList = new ArrayList<>();
        Cassettes cassettes = new Cassettes(cassetteList);

        cassettes.add(new CoinCassette("NICKEL", 5));

        cassettes.reset();

        assertThat(cassetteList.size(), is(1));
        assertThat(cassetteList.get(0).getId(), is("NICKEL"));
        assertThat(cassetteList.get(0).getQuantity(), is(100));
    }

    @Test
    public void testEjectCoin() {

        CoinCassette coinCassette = new CoinCassette("NICKEL", 5);
        Cassettes cassettes = new Cassettes(singletonList(coinCassette));

        cassettes.ejectCoin("NICKEL");

        assertThat(coinCassette.getQuantity(), is(4));
    }

    @Test
    public void testEjectCoin_forInvalidDenomination() {
        Cassettes cassettes = new Cassettes(emptyList());

        try {
            cassettes.ejectCoin("NICKEL");
            fail("expected exception");
        } catch (SystemException e) {
            assertThat(e.getMessage(), is("Coin denomination not found."));
        }
    }

    @Test
    public void testGetCoinCount() {
        Cassettes cassettes = new Cassettes(singletonList(new CoinCassette("NICKEL", 5)));

        int response = cassettes.getCoinCount("NICKEL");

        assertThat(response, is(5));
    }

    @Test
    public void testGetCoinCount_withInvalidId() {
        Cassettes cassettes = new Cassettes(emptyList());

        int count = cassettes.getCoinCount("NICKEL");

        assertThat(count, is(0));
    }
}
