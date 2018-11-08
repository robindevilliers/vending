package com.robindevilliers.vending;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CoinPurse {

    private Map<String, Integer> coins = new HashMap<>();

    public void addCoin(String denomination) {
        coins.put(denomination, coins.getOrDefault(denomination, 0) + 1);
    }

    public void refund(List<String> responses) {

        coins.forEach((denomination, coinCount) -> {
            IntStream.range(0, coinCount).forEach(i -> responses.add(denomination));
        });
    }

    public Map<String, Integer> getCounts() {
        return Collections.unmodifiableMap(this.coins);
    }

    public void dropPaymentIntoSafe() {
        this.coins = new HashMap<>();
    }
}
