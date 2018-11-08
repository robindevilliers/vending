package com.robindevilliers.vending.model;

public class Denomination {
    private final String id;
    private final int amount;

    public Denomination(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
}
