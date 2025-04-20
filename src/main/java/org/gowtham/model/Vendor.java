package org.gowtham.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Vendor {
    private int id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private Timestamp createdAt;
    private String formattedCreatedAt;

    public Vendor() {
    }

    public Vendor(int id, String name, String contactPerson, String phone, String email, String address, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
        setFormattedCreatedAt();
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        setFormattedCreatedAt();
    }

    public String getFormattedCreatedAt() {
        if (formattedCreatedAt == null && createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedCreatedAt = sdf.format(createdAt);
        }
        return formattedCreatedAt;
    }

    private void setFormattedCreatedAt() {
        if (createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.formattedCreatedAt = sdf.format(this.createdAt);
        } else {
            this.formattedCreatedAt = null;
        }
    }
}
