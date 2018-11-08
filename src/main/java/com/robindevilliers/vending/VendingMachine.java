package com.robindevilliers.vending;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class VendingMachine {

    private final CoinCalculator coinCalculator;

    private final ItemBuckets itemBuckets;

    private final Cassettes cassettes;

    private final CoinPurse coinPurse;

    public VendingMachine(CoinCalculator coinCalculator, ItemBuckets itemBuckets, Cassettes cassettes, CoinPurse coinPurse) {
        this.coinCalculator = coinCalculator;
        this.itemBuckets = itemBuckets;
        this.cassettes = cassettes;
        this.coinPurse = coinPurse;
    }

    public String process(String line) {
        final List<String> responses = new ArrayList<>();
        final List<String> commands = Stream.of(line.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        for (String command : commands) {
            if (command.equals("SERVICE")) {
                serviceMachine(responses);
            } else if (command.startsWith("PUT-")) {
                enterCoin(responses, command);
            } else if (command.equals("COIN-RETURN")) {
                coinReturn(responses);
            } else if (command.startsWith("GET-")) {
                purchaseItem(responses, command.substring(4));
            } else {
                responses.add("INVALID-COMMAND");
            }
        }
        return responses.stream().collect(Collectors.joining(", "));
    }

    private void coinReturn(List<String> responses) {
        this.coinPurse.refund(responses);
    }

    private void enterCoin(List<String> responses, String command) {
        final String denomination = command.substring(4);

        if (this.coinCalculator.hasDenomination(denomination)) {
            this.coinPurse.addCoin(denomination);
        } else {
            responses.add("INVALID-COIN");
        }
    }

    private void purchaseItem(List<String> responses, String itemId) {
        if (!this.itemBuckets.isValidItemId(itemId)) {
            responses.add("INVALID-ITEM");
            return;
        }

        if (!this.itemBuckets.hasItem(itemId)) {
            responses.add("OUT-OF-STOCK");
            return;
        }

        final int currentFunds = this.coinCalculator.calculateTotal(this.coinPurse);

        final int fundsRequired = this.itemBuckets.getPriceForItem(itemId);

        if (currentFunds < fundsRequired) {
            responses.add("INSUFFICIENT-FUNDS");
            return;
        }

        final int changeRequired = currentFunds - fundsRequired;

        List<String> coins;
        try {
            coins = this.coinCalculator.calculateChange(changeRequired, this.cassettes);
        } catch (InsufficientChangeException e) {
            responses.add("INSUFFICIENT-CHANGE");
            return;
        }

        this.coinPurse.dropPaymentIntoSafe();

        itemBuckets.ejectItem(itemId);
        responses.add(itemId);

        responses.addAll(coins);
    }

    private void serviceMachine(List<String> responses) {

        this.itemBuckets.reset();

        this.cassettes.reset();

        responses.add("SERVICE-OK");
    }
}
