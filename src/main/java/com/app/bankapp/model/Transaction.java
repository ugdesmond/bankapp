package com.app.bankapp.model;

import com.app.bankapp.model.enums.TransactionTypeEnum;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(targetEntity = Account.class)
    private Account accountTransferFrom;
    @ManyToOne(targetEntity = Account.class)
    private Account accountTransferTo;
    @ManyToOne(targetEntity = Account.class)
    private Account depositWithdrawalAccount;
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum  transactionType;
    private Date transactionDateTime;
    private Double amount;
    @ManyToOne(targetEntity = User.class)
    private User createBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getAccountTransferFrom() {
        return accountTransferFrom;
    }

    public void setAccountTransferFrom(Account accountTransferFrom) {
        this.accountTransferFrom = accountTransferFrom;
    }

    public Account getAccountTransferTo() {
        return accountTransferTo;
    }

    public void setAccountTransferTo(Account accountTransferTo) {
        this.accountTransferTo = accountTransferTo;
    }

    public Account getDepositWithdrawalAccount() {
        return depositWithdrawalAccount;
    }

    public void setDepositWithdrawalAccount(Account depositWithdrawalAccount) {
        this.depositWithdrawalAccount = depositWithdrawalAccount;
    }

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }
}
