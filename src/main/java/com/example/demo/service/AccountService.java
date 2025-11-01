package com.example.demo.service;

import com.example.demo.model.Account_User;
import com.example.demo.repo.AccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;

    public List<Account_User> getAll() {
        try {
            log.info("Fetching all accounts...");
            List<Account_User> accounts = accountRepo.findAll();
            log.info("Fetched {} accounts successfully", accounts.size());
            return accounts;
        } catch (Exception e) {
            log.error("Error while fetching all accounts", e);
            throw e;
        }
    }

    public Account_User createAccount(Account_User account) {
        try {
            log.info("Creating new account for username: {}", account.getUsername());
            if (accountRepo.existsByUsername(account.getUsername())) {
                log.warn("Username '{}' already exists", account.getUsername());
                throw new RuntimeException("Username already exists");
            }

            account.setPassword(passwordEncoder.encode(account.getPassword()));
            Account_User saved = accountRepo.save(account);
            log.info("Account created successfully for username: {}", saved.getUsername());
            return saved;
        } catch (Exception e) {
            log.error("Error while creating account for username: {}", account.getUsername(), e);
            throw e;
        }
    }

    public Account_User updateAccount(Long id, Account_User account) {
        try {
            log.info("Updating account with ID: {}", id);
            Account_User existing = accountRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Account not found with ID: {}", id);
                        return new RuntimeException("User not found");
                    });

            existing.setPassword(passwordEncoder.encode(account.getPassword()));
            existing.setRole(account.getRole());

            Account_User updated = accountRepo.save(existing);
            log.info("Account updated successfully for username: {}", updated.getUsername());
            return updated;
        } catch (Exception e) {
            log.error("Error while updating account with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteAccount(String username) {
        try {
            log.info("Deleting account with username: {}", username);
            Account_User user = accountRepo.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("Account not found with username: {}", username);
                        return new RuntimeException("User not found");
                    });
            accountRepo.delete(user);
            log.info("Account deleted successfully: {}", username);
        } catch (Exception e) {
            log.error("Error while deleting account with username: {}", username, e);
            throw e;
        }
    }

    public Account_User getAccount_user(long id) {
        try {
            log.info("Fetching account with ID: {}", id);
            Account_User account = accountRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Account not found with ID: {}", id);
                        return new RuntimeException("User not found");
                    });
            log.info("Account fetched successfully: {}", account.getUsername());
            return account;
        } catch (Exception e) {
            log.error("Error fetching account with ID: {}", id, e);
            throw e;
        }
    }
}
