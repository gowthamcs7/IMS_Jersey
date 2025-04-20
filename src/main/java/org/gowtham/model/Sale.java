package org.gowtham.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class Sale {
    private int id;
    private int customerID;
    private int userID;
    private double totalAmount;
    private Timestamp createdAt;
    private String formattedCreatedAt;
    private List<SaleItem> saleItems;

    public Sale(){}
    public Sale(int id, int customerID, int userID, double totalAmount, Timestamp saleDate, List<SaleItem> saleItems){
        this.id=id;
        this.customerID=customerID;
        this.userID=userID;
        this.totalAmount=totalAmount;
        this.createdAt =saleDate;
        this.saleItems = saleItems;
        setFormattedCreatedAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        setFormattedCreatedAt();

    }

    public List<SaleItem> getSaleItems() { return saleItems; }
    public void setSaleItems(List<SaleItem> saleItems) { this.saleItems = saleItems; }

    public void setFormattedCreatedAt() {
        if (this.createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.formattedCreatedAt = sdf.format(this.createdAt);
        } else {
            this.formattedCreatedAt = null; // Prevent NullPointerException
        }
    }


    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("formattedCreatedAt")
    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }
}
