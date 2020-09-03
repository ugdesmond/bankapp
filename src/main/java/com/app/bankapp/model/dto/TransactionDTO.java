package com.app.bankapp.model.dto;

import java.sql.Timestamp;

public class TransactionDTO {
    private String transactionType;
    private Timestamp transactionDateTime;
    private Double amount;
    private String accountTransferFrom;
    private String accountTransferTo;
    private String depositWithdrawalAccount;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountTransferFrom() {
        return accountTransferFrom;
    }

    public void setAccountTransferFrom(String accountTransferFrom) {
        this.accountTransferFrom = accountTransferFrom;
    }

    public String getAccountTransferTo() {
        return accountTransferTo;
    }

    public void setAccountTransferTo(String accountTransferTo) {
        this.accountTransferTo = accountTransferTo;
    }

    public String getDepositWithdrawalAccount() {
        return depositWithdrawalAccount;
    }

    public Timestamp getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Timestamp transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public void setDepositWithdrawalAccount(String depositWithdrawalAccount) {
        this.depositWithdrawalAccount = depositWithdrawalAccount;
    }
}
