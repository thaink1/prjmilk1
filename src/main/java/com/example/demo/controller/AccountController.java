package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Account_User;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;
    ObjectMapper objectMapper;

    @GetMapping("")
    BaseResponse<List<Account_User>> getAll() {
        BaseResponse<List<Account_User>> response = new BaseResponse<>();
        response.setBody(accountService.getAll());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    BaseResponse<Account_User> getUser(@PathVariable Long id) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        response.setBody(accountService.getAccount_user(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    BaseResponse<Account_User> createAccount(@RequestBody @Valid Account_User account) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        response.setBody(accountService.createAccount(account));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    BaseResponse<Account_User> updateAccount(@PathVariable Long id, @Valid @RequestBody Account_User account) {
        BaseResponse<Account_User> response = new BaseResponse<>();
        response.setBody(accountService.updateAccount(id, account));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{username}")
    BaseResponse<Void> deleteAccount(@PathVariable String username) {
        accountService.deleteAccount(username);
        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage(username + " has been deleted successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

}
