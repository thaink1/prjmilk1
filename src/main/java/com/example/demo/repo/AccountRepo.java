package com.example.demo.repo;

import com.example.demo.model.Account_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account_User, Long> {
    boolean existsByUsername(String username);
    Optional<Account_User> findByUsername(String username);


}
