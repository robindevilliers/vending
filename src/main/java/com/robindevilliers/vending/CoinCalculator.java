package com.robindevilliers.vending;

import com.robindevilliers.vending.model.Denomination;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class CoinCalculator {

    private final List<Denomination> denominations;

    public CoinCalculator(List<Denomination> denominations) {
        this.denominations = denominations;
        denominations.sort(Comparator.comparingInt(Denomination::getAmount).reversed());
    }

    public boolean hasDenomination(String id) {
        return this.denominations.stream().anyMatch(denomination -> denomination.getId().equals(id));
    }

    public int calculateTotal(CoinPurse coinPurse) {
        return coinPurse.getCounts()
                .entrySet()
                .stream()
                .mapToInt(entry ->
                        this.denominations.stream()
                                .filter(denomination -> denomination.getId().equals(entry.getKey()))
                                .map(Denomination::getAmount)
                                .findAny()
                                .orElseThrow(() -> new SystemException("Invalid denomination in coin purse."))
                                * entry.getValue())
                .sum();
    }

    public List<String> calculateChange(int changeRequired, Cassettes cassettes) throws InsufficientChangeException {
        final List<String> coins = new ArrayList<>();

        final Queue<Denomination> denominations = new ArrayDeque<>(this.denominations);

        while (changeRequired != 0) {
            final Denomination denomination = denominations.poll(); //these denominations should be ordered from greatest to smallest.

            if (denomination == null) {
                throw new InsufficientChangeException();
            }

            final int coinsWanted = changeRequired / denomination.getAmount();

            final int coinsAvailable = cassettes.getCoinCount(denomination.getId());

            final int coinsToTake = Math.min(coinsAvailable, coinsWanted);

            changeRequired = changeRequired - (coinsToTake * denomination.getAmount());

            IntStream.range(0,coinsToTake).forEach((i) -> coins.add(denomination.getId()));
        }

        //we only eject coins here after we are sure that we have sufficient change.
        coins.forEach(cassettes::ejectCoin);

        return coins;
    }


}
