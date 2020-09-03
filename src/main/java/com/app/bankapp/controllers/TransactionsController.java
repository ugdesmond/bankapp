package com.app.bankapp.controllers;

import com.app.bankapp.businesslogic.AccountLogic;
import com.app.bankapp.businesslogic.TransactionLogic;
import com.app.bankapp.businesslogic.UserLogic;
import com.app.bankapp.model.Account;
import com.app.bankapp.model.MessageResponse;
import com.app.bankapp.model.Transaction;
import com.app.bankapp.model.User;
import com.app.bankapp.model.dto.TransactionDTO;
import com.app.bankapp.model.enums.StatusEnum;
import com.app.bankapp.model.enums.TransactionTypeEnum;
import com.app.bankapp.model.viewmodels.DepositWithdrawViewModel;
import com.app.bankapp.model.viewmodels.TransactionViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    TransactionLogic transactionLogic;
    AccountLogic accountLogic;
    UserLogic userLogic;

    public TransactionsController(TransactionLogic transactionLogic, AccountLogic accountLogic, UserLogic userLogic) {
        this.transactionLogic = transactionLogic;
        this.accountLogic = accountLogic;
        this.userLogic = userLogic;
    }

    /**
     * This controller allows user to make a deposit.
     *
     * @param transactionViewModel Request  body {@link TransactionViewModel} class
     * @return A {@link ResponseEntity <>} containing the {@link TransactionDTO} .
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(method = RequestMethod.POST, value = "/deposit")
    public ResponseEntity<MessageResponse<TransactionDTO>> depositFund(@Valid @RequestBody DepositWithdrawViewModel transactionViewModel) throws Exception {
        MessageResponse<TransactionDTO> messageResponse;
        try {
            List<Account> accounts = accountLogic.getByColumnName("accountNumber", transactionViewModel.getAccountNumber());
            if (accounts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            List<User> userList = userLogic.getByColumnName("email", auth.getPrincipal());
            Account account = accounts.get(0);
            if (account.getStatus().equals(StatusEnum.CLOSED)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account has been closed!");
            }
            Transaction transaction = new Transaction();
            transaction.setDepositWithdrawalAccount(account);
            transaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            transaction.setAmount(transactionViewModel.getAmount());
            transaction.setTransactionDateTime(new Date());
            transaction.setCreateBy(userList.get(0));
            transactionLogic.create(transaction);
            account.setAvailableBalance(account.getTotalBalance() + transactionViewModel.getAmount());
            account.setTotalBalance(account.getTotalBalance() + transactionViewModel.getAmount());
            account.setLastUpdateDateTime(new Timestamp(new Date().getTime()));
            accountLogic.update(account);
            TransactionDTO transactionDTO = transactionLogic.convertToDto(transaction, TransactionDTO.class);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account deposit  successfully");
            messageResponse.setData(transactionDTO);
            messageResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    /**
     * This controller enables user to transfer fund from one account to the other.
     *
     * @param transactionViewModel Request  body {@link TransactionViewModel} class
     * @return A {@link ResponseEntity <>} containing the {@link TransactionDTO} .
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(method = RequestMethod.POST, value = "/transfer")
    public ResponseEntity<MessageResponse<TransactionDTO>> transferFunds(@Valid @RequestBody TransactionViewModel transactionViewModel) throws Exception {
        MessageResponse<TransactionDTO> messageResponse;
        try {
            List<Account> senderAccountList = accountLogic.getByColumnName("accountNumber", transactionViewModel.getAccountTransferFrom());
            if (senderAccountList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender account does not exist!");
            }
            List<Account> recipientAccountList = accountLogic.getByColumnName("accountNumber", transactionViewModel.getAccountTransferTo());
            if (recipientAccountList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient account does not exist!");
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            List<User> userList = userLogic.getByColumnName("email", auth.getPrincipal());


            Account senderAccount = senderAccountList.get(0);
            if (senderAccount.getStatus().equals(StatusEnum.CLOSED)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Sender account has been closed!");
            }
            Double balance = senderAccount.getAvailableBalance();

            Account recipientAccount = recipientAccountList.get(0);
            if (recipientAccount.getStatus().equals(StatusEnum.CLOSED)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Recipient  account has been closed!");
            }
            if (senderAccount.getId().equals(recipientAccount.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot transfer funds to same account");
            }
            if (transactionViewModel.getAmount() > balance) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
            }
            recipientAccount.setAvailableBalance(recipientAccount.getAvailableBalance() + transactionViewModel.getAmount());
            recipientAccount.setTotalBalance(recipientAccount.getTotalBalance() + transactionViewModel.getAmount());
            accountLogic.update(recipientAccount);

            senderAccount.setTotalBalance(senderAccount.getTotalBalance() - transactionViewModel.getAmount());
            senderAccount.setAvailableBalance(senderAccount.getAvailableBalance() - transactionViewModel.getAmount());
            accountLogic.update(senderAccount);
            Transaction transaction = new Transaction();
            transaction.setAccountTransferFrom(senderAccount);
            transaction.setAccountTransferTo(recipientAccount);
            transaction.setTransactionType(TransactionTypeEnum.FUND_TRANSFER);
            transaction.setAmount(transactionViewModel.getAmount());
            transaction.setTransactionDateTime(new Date());
            transaction.setCreateBy(userList.get(0));
            transactionLogic.create(transaction);
            TransactionDTO transactionDTO = transactionLogic.convertToDto(transaction, TransactionDTO.class);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Transfer successfully");
            messageResponse.setData(transactionDTO);
            messageResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * This controller  retrieves all user transactions with account number.
     *
     * @param accountNumber Request  body {@link String} class
     * @return A {@link ResponseEntity <>} containing the {@link TransactionDTO} .
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<TransactionDTO>>> getAllTransactionsByAccountNumber(@RequestParam @NotNull String accountNumber) throws Exception {
        MessageResponse<List<TransactionDTO>> messageResponse;
        try {
            List<Account> accounts = accountLogic.getByColumnName("accountNumber", accountNumber);
            if (accounts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account number  does not exist!");
            }
            List<Transaction> transactions = transactionLogic.getUserTransactions(accounts.get(0));
            List<TransactionDTO> transactionDTOList = transactions.stream().map(r -> transactionLogic.convertToDto(r, TransactionDTO.class)).collect(Collectors.toList());
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Retrieved successfully");
            messageResponse.setData(transactionDTOList);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
