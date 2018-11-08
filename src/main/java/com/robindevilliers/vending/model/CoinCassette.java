package com.robindevilliers.vending.model;

public class CoinCassette {

    private final String id;
    private int quantity;

    public CoinCassette(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
