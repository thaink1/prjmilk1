package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Account_User;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public BaseResponse<List<Account_User>> getAll() {
        BaseResponse<List<Account_User>> response = new BaseResponse<>();
        try {
            log.info("Request to get all accounts");
            List<Account_User> accounts = accountService.getAll();
            response.setBody(accounts);
            response.setMessage("Fetched all accounts successfully");
        } catch (Exception e) {
            log.error("Error fetching all accounts", e);
            // Không setCode, để GlobalExceptionHandler xử lý
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Account_User> getUser(@PathVariable Long id) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        try {
            log.info("Request to get account with ID: {}", id);
            Account_User account = accountService.getAccount_user(id);
            response.setBody(account);
            response.setMessage("Fetched account successfully");
        } catch (Exception e) {
            log.error("Error fetching account with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Account_User> createAccount(@Valid @RequestBody Account_User account) {
        log.info("Request to create account for username: {}", account.getUsername());
        Account_User created = accountService.createAccount(account);

        BaseResponse<Account_User> response = new BaseResponse<>();
        response.setMessage("Account created successfully");
        response.setBody(created);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Account_User> updateAccount(@PathVariable Long id, @Valid @RequestBody Account_User account) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        try {
            log.info("Request to update account with ID: {}", id);
            Account_User updated = accountService.updateAccount(id, account);
            response.setBody(updated);
            response.setMessage("Account updated successfully");
        } catch (Exception e) {
            log.error("Error updating account with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{username}")
    public BaseResponse<Void> deleteAccount(@PathVariable String username) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete account: {}", username);
            accountService.deleteAccount(username);
            response.setMessage(username + " has been deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting account: {}", username, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
