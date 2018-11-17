package com.example.kuba.exercise_01;

import java.io.Serializable;

public class Item implements Serializable {

    private int id;
    private String name;
    private int price;
    private int quantity;
    private boolean isBought;

    public Item() { }

    public Item (int id, String name, int price, int quantity, boolean isBought) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isBought = isBought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
