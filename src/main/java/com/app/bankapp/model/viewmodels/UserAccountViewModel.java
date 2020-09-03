package com.app.bankapp.model.viewmodels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserAccountViewModel {
    @NotEmpty
    @NotNull
    private String fullName;
    @NotEmpty
    @NotNull
    private String email;
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
