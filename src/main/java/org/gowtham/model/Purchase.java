package org.gowtham.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) // Ensures null fields are included


public class Purchase {
    private int id;
    private int userId;
    private int vendorId;
    private double totalAmount;
    private Timestamp createdAt;
    private List<PurchaseItem> purchaseItemList;
    private String formattedCreatedAt;
    private String userName;
    private String vendorName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Purchase(){

    }
    public Purchase(int id, int userId, String userName,int vendorId, String vendorName, double totalAmount, Timestamp createdAt, List<PurchaseItem> purchaseItemList){
        this.id= id;
        this.userId=userId;
        this.userName=userName;
        this.vendorId=vendorId;
        this.vendorName=vendorName;
        this.totalAmount=totalAmount;
        this.createdAt = createdAt;
        this.purchaseItemList = purchaseItemList;
        setFormattedCreatedAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public List<PurchaseItem> getPurchaseItemList() {
        return purchaseItemList;
    }

    public void setPurchaseItemList(List<PurchaseItem> purchaseItemList) {
        this.purchaseItemList = purchaseItemList;
    }






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
