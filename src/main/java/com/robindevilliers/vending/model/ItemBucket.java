package com.robindevilliers.vending.model;

public class ItemBucket {
    private final String id;
    private final int price;
    private int quantity;

    public ItemBucket(String id, int price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;

    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }
}
