package org.gowtham.model;

public class PurchaseItem {

    private int itemId;
    private String itemName;
    private int qty;
    private double purchasePrice;
    private double totalPrice;

    public PurchaseItem(){}
    public PurchaseItem(int itemId, String itemName, int qty,double purchasePrice, double totalPrice){

        this.itemId=itemId;
        this.itemName=itemName;
        this.qty=qty;
        this.purchasePrice=purchasePrice;
        this.totalPrice=totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
