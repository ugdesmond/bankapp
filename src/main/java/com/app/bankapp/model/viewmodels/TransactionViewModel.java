package com.app.bankapp.model.viewmodels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransactionViewModel {
    @NotEmpty
    @NotNull
    private String  accountTransferTo;
    @NotEmpty
    @NotNull
    private String  accountTransferFrom;
    @NotNull
    private Double amount;

    public String getAccountTransferTo() {
        return accountTransferTo;
    }

    public void setAccountTransferTo(String accountTransferTo) {
        this.accountTransferTo = accountTransferTo;
    }

    public String getAccountTransferFrom() {
        return accountTransferFrom;
    }

    public void setAccountTransferFrom(String accountTransferFrom) {
        this.accountTransferFrom = accountTransferFrom;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
