package com.app.bankapp.controllers;

import com.app.bankapp.businesslogic.AccountLogic;
import com.app.bankapp.businesslogic.UserLogic;
import com.app.bankapp.model.Account;
import com.app.bankapp.model.MessageResponse;
import com.app.bankapp.model.User;
import com.app.bankapp.model.dto.AccountDTO;
import com.app.bankapp.model.enums.StatusEnum;
import com.app.bankapp.model.enums.UserRoleEnum;
import com.app.bankapp.model.viewmodels.UserAccountViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    AccountLogic accountLogic;
    UserLogic userLogic;

    public AccountController(AccountLogic accountLogic, UserLogic userLogic) {
        this.accountLogic = accountLogic;
        this.userLogic = userLogic;
    }

    /**
     * This controller creates user accounts
     *
     * @param userAccount Request  body {@link UserAccountViewModel} class
     * @return A {@link ResponseEntity <>} containing the {@link AccountDTO} .
     */
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<AccountDTO>> createUserAccount(@Valid @RequestBody UserAccountViewModel userAccount) throws Exception {
        MessageResponse<AccountDTO> messageResponse;
        try {
            List<User> userList = userLogic.getByColumnName("email", userAccount.getEmail());
            if (!userList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User account already exist!");
            }
            User user = new User();
            Account account = new Account();
            user.setEmail(userAccount.getEmail());
            user.setFullName(userAccount.getFullName());
            user.setRole(UserRoleEnum.USER);
            user.setPhoneNumber(userAccount.getPhoneNumber());
            userLogic.create(user);
            account.setUser(user);
            account.setStatus(StatusEnum.ACTIVE);
            account.setDateTimeCreated(new Timestamp(new Date().getTime()));
            account.setLastUpdateDateTime(account.getDateTimeCreated());
            account.setAvailableBalance(0.00);
            account.setTotalBalance(0.00);
            account.setAccountNumber(accountLogic.generateAccountNumber());
            accountLogic.create(account);
            AccountDTO accountDTO = accountLogic.convertToDto(account, AccountDTO.class);
            accountDTO.setAccountName(user.getFullName());
            accountDTO.setEmail(user.getEmail());
            accountDTO.setAccountId(account.getId());
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account created successfully");
            messageResponse.setData(accountDTO);
            messageResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }




    /**
     * This controller closes user accounts
     *
     * @param id Request  body {@link String} class
     * @return A {@link ResponseEntity <>} containing the {@link AccountDTO}.
     */
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT,value = "/{id}/deactivate")
    public ResponseEntity<MessageResponse<AccountDTO>> closeUserAccount(@PathVariable Integer id) throws Exception {
        MessageResponse<AccountDTO> messageResponse;
        try {
           Account account = accountLogic.findOne(id);
            if (account == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
            }
            account.setStatus(StatusEnum.CLOSED);
            accountLogic.update(account);
            AccountDTO accountDTO = accountLogic.convertToDto(account, AccountDTO.class);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account closed successfully");
            messageResponse.setData(accountDTO);
            messageResponse.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    /**
     * This controller retrieves user account
     *
     * @param accountNumber Request  body {@link String} class
     * @return A {@link ResponseEntity <>} containing the {@link AccountDTO}.
     */
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<AccountDTO>> getUserAccount(@RequestParam @NotNull String accountNumber) throws Exception {
        MessageResponse<AccountDTO> messageResponse;
        try {
            List<Account> accountList = accountLogic.getByColumnName("accountNumber",accountNumber);
            if (accountList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
            }
            Account account= accountList.get(0);
            AccountDTO accountDTO = accountLogic.convertToDto(account, AccountDTO.class);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account retrieved successfully");
            messageResponse.setData(accountDTO);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
