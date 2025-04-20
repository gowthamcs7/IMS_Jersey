package org.gowtham.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Item {
    private int id;
    private String name;
    private int categoryId;
    private double averageCostPrice;
    private double defaultSellingPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String formattedCreatedAt;
    private String formattedUpdatedAt;
    private String categoryName;
    private String categoryDescription;
    private int stockQuantity;
    public Item(){}
    public Item(int id, String name, int categoryId, String categoryName, String categoryDescription,
                double averageCostPrice, double defaultSellingPrice, Timestamp createdAt, Timestamp updatedAt,
                int stockQuantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.averageCostPrice = averageCostPrice;
        this.defaultSellingPrice = defaultSellingPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stockQuantity = stockQuantity;

        setFormattedCreatedAt();
        setFormattedUpdatedAt();
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() { return categoryName; }
    public String getCategoryDescription() { return categoryDescription; }


    public double getAverageCostPrice() {
        return averageCostPrice;
    }

    public void setAverageCostPrice(double averageCostPrice) {
        this.averageCostPrice = averageCostPrice;
    }

    public double getDefaultSellingPrice() {
        return defaultSellingPrice;
    }

    public void setDefaultSellingPrice(double defaultSellingPrice) {
        this.defaultSellingPrice = defaultSellingPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }

    public void setFormattedCreatedAt() {
        if (this.createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.formattedCreatedAt = sdf.format(this.createdAt);
        } else {
            this.formattedCreatedAt = null; // Prevent NullPointerException
        }
    }

    public void setFormattedUpdatedAt() {
        if (this.updatedAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.formattedUpdatedAt = sdf.format(this.updatedAt);
        } else {
            this.formattedUpdatedAt = null; // Prevent NullPointerException
        }
    }
}
