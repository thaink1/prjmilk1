package com.example.demo.service;

import com.example.demo.model.Account_User;
import com.example.demo.repo.AccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private AccountRepo accountRepo;
    public List<Account_User> getAll() {
        return accountRepo.findAll();
    }
    public Account_User createAccount(Account_User account) {
        if (accountRepo.existsByUsername(account.getUsername())) {
            throw new RuntimeException( "Username already exists");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepo.save(account);
    }

    public Account_User updateAccount(Long id, Account_User account) {
        Account_User acc = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found" ));
        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            acc.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        if (account.getRole() != null) {
            acc.setRole(account.getRole());
        }
        return accountRepo.save(acc);
    }

    public void deleteAccount(String username) {
        Account_User user = accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        accountRepo.delete(user);
    }
    public Account_User getAccount_user(long id) {
        return accountRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
