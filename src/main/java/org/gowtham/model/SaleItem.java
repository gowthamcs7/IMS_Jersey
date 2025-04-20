package org.gowtham.model;

public class SaleItem {
    private int id;
    private int saleId;
    private int itemId;
    private int qty;
    private double salePrice;
    private double totalPrice;

    public SaleItem(){}
    public SaleItem(int id,int saleId,int itemId,int qty, double salePrice,double totalPrice){
        this.id=id;
        this.saleId=saleId;
        this.itemId=itemId;
        this.qty=qty;
        this.salePrice=salePrice;
        this.totalPrice=totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
