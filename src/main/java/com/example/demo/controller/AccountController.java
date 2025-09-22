package com.example.demo.controller;

import com.example.demo.model.Account_User;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;
    ObjectMapper objectMapper;

    @GetMapping("")
    public List<Account_User> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Account_User getUser(@PathVariable Long id) {
        return accountService.getAccount_user(id);
    }

    @PostMapping("")
    public Account_User createAccount(@RequestBody Account_User account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/{id}")
    public Account_User updateAccount(@PathVariable Long id, @RequestBody Account_User account) {
        return  accountService.updateAccount(id, account);
    }

    @DeleteMapping("/{username}")
    String deleteAccount(@PathVariable String username) {
        accountService.deleteAccount(username);
        return "User has been deleted" ;
    }

}
