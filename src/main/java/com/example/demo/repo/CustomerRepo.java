package com.example.demo.repo;

import com.example.demo.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByPhone(String phone);
    boolean existsCustomerByPhone(String phone);
    boolean existsCustomerByEmail(String email);
    boolean existsByEmailAndCustomerIdNot(String email, Long customerId);
    boolean existsByPhoneAndCustomerIdNot(String phone, Long customerId);
}
