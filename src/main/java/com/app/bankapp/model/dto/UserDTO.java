package com.app.bankapp.model.dto;

import java.util.Date;

public class UserDTO {
    Integer id;
    String email;
    String token;
    String fullName;
    Date tokenExpiryTime;
    String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    public void setTokenExpiryTime(Date tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }
}
