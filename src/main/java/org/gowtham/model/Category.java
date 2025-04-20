package org.gowtham.model;

import java.sql.Timestamp;

public class Category {
    private int id;
    private String name;
    private String description;
    private Timestamp createdAt;

    public Category(){}
    public Category(int id, String name,String description,Timestamp createdAt){
        this.id=id;
        this.name=name;
        this.description=description;
        this.createdAt=createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
