package org.gowtham.model;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class User {
    private long id ;
    private String name;
    private String email;
    private String password;
    private String role;

    private Timestamp createdAt;
    private String formattedCreatedAt;

    public User(){

    }
    public User(long id, String email, String password, String role, Timestamp createdAt) {
        this.id = id;
        this.name = "Default Name"; // Default name
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        setFormattedCreatedAt();
    }


    public User(long id, String name, String email, String password, String role, Timestamp createdAt){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=role;
        this.createdAt=createdAt;
        setFormattedCreatedAt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setFormattedCreatedAt() {
        if (this.createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.formattedCreatedAt = sdf.format(this.createdAt);
        } else {
            this.formattedCreatedAt = null; // Prevent NullPointerException
        }
    }


    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }
}
