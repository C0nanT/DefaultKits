package com.example.plugin.kit;

public class KitItem {
    private final String itemId;
    private final int quantity;

    public KitItem(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
