package com.app.bankapp.model.viewmodels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DepositWithdrawViewModel {
    @NotEmpty
    @NotNull
    private String  accountNumber;

    @NotNull
    private Double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
