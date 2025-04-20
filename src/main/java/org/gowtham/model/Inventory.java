package org.gowtham.model;

import java.sql.Timestamp;

public class Inventory {
    private int id;
    private int itemId;
    private int stockQuantity;
    private Timestamp lastUpdated;
    public Inventory(){}
    public Inventory(int id, int itemId, int stockQuantity, Timestamp lastUpdated){
        this.id=id;
        this.itemId=itemId;
        this.stockQuantity=stockQuantity;
        this.lastUpdated=lastUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
