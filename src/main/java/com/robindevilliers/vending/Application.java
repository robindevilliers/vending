package com.robindevilliers.vending;

import static java.util.Arrays.asList;

import com.robindevilliers.vending.model.CoinCassette;
import com.robindevilliers.vending.model.Denomination;
import com.robindevilliers.vending.model.ItemBucket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Application {
    public static void main(String[] args) throws IOException {

        final CoinCalculator coinCalculator = new CoinCalculator(asList(
                new Denomination("DOLLAR", 100),
                new Denomination("DIME", 10),
                new Denomination("NICKEL", 5),
                new Denomination("QUARTER", 25)
        ));

        final ItemBuckets itemBuckets = new ItemBuckets(new ArrayList<>());
        itemBuckets.add(new ItemBucket("A", 65, 20));
        itemBuckets.add(new ItemBucket("B", 100, 20));
        itemBuckets.add(new ItemBucket("C", 150, 20));

        final Cassettes cassettes = new Cassettes(new ArrayList<>());

        cassettes.add(new CoinCassette("NICKEL", 100));
        cassettes.add(new CoinCassette("DIME", 100));
        cassettes.add(new CoinCassette("QUARTER", 100));
        cassettes.add(new CoinCassette("DOLLAR", 100));

        final VendingMachine vendingMachine = new VendingMachine(coinCalculator, itemBuckets, cassettes, new CoinPurse());

        final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");

            final String line = buffer.readLine();
            final String response = vendingMachine.process(line);
            System.out.print("-> ");
            System.out.println(response);
        }
    }
}
