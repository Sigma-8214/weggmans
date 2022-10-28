package com.connorcode;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Item {
    String name;
    double price;
    int stock;

    Item(JsonElement ele) {
        JsonObject obj = ele.getAsJsonObject();
        this.name = obj.get("name")
                .getAsString();
        this.price = obj.get("price")
                .getAsDouble();
        this.stock = obj.get("stock")
                .getAsInt();
    }

    public static class CartItem {
        int stockIndex;
        int count;

        CartItem(int index, int count) {
            this.stockIndex = index;
            this.count = count;
        }
    }
}
