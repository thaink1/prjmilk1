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
            response.setBody(accountService.getAll());
            response.setMessage("Fetched all accounts successfully");
        } catch (Exception e) {
            log.error("Error fetching all accounts", e);
            response.setMessage("Failed to fetch accounts: " + e.getMessage());
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
            response.setBody(accountService.getAccount_user(id));
            response.setMessage("Fetched account successfully");
        } catch (Exception e) {
            log.error("Error fetching account with ID: {}", id, e);
            response.setMessage("Failed to fetch account: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Account_User> createAccount(@Valid @RequestBody Account_User account) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        try {
            log.info("Request to create account for username: {}", account.getUsername());
            response.setBody(accountService.createAccount(account));
            response.setMessage("Account created successfully");
        } catch (Exception e) {
            log.error("Error creating account for username: {}", account.getUsername(), e);
            response.setMessage("Failed to create account: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Account_User> updateAccount(@PathVariable Long id, @Valid @RequestBody Account_User account) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        try {
            log.info("Request to update account with ID: {}", id);
            response.setBody(accountService.updateAccount(id, account));
            response.setMessage("Account updated successfully");
        } catch (Exception e) {
            log.error("Error updating account with ID: {}", id, e);
            response.setMessage("Failed to update account: " + e.getMessage());
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
            response.setMessage("Failed to delete account: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
