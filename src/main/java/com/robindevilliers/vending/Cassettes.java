package com.robindevilliers.vending;

import com.robindevilliers.vending.model.CoinCassette;

import java.util.List;

public class Cassettes {

    private final List<CoinCassette> cassettes;

    public Cassettes(List<CoinCassette> cassettes) {
        this.cassettes = cassettes;
    }

    public void add(CoinCassette coinCassette) {
        this.cassettes.add(coinCassette);
    }

    public void reset() {
        this.cassettes.forEach(cassette -> cassette.setQuantity(100));
    }

    public void ejectCoin(String denomination) {
        final CoinCassette coinCassette = this.cassettes.stream()
                .filter(cassette -> cassette.getId().equals(denomination))
                .findAny()
                .orElseThrow(() -> new SystemException("Coin denomination not found."));

        coinCassette.setQuantity(coinCassette.getQuantity() - 1);
    }

    public int getCoinCount(String id) {
        return this.cassettes.stream()
                .filter(cassette -> cassette.getId().equals(id))
                .map(CoinCassette::getQuantity)
                .findAny()
                .orElse(0);
    }
}
