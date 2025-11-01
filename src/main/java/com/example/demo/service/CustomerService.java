package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repo.CustomerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepo customerRepo;

    public List<Customer> getAllCustomers() {
        try {
            log.info("Fetching all customers...");
            List<Customer> customers = customerRepo.findAll();
            log.info("Fetched {} customers successfully", customers.size());
            return customers;
        } catch (Exception e) {
            log.error("Error while fetching all customers", e);
            throw e;
        }
    }

    public Customer findByPhone(String phone) {
        try {
            log.info("Fetching customer by phone: {}", phone);
            if (!customerRepo.existsCustomerByPhone(phone)) {
                log.warn("Customer not found with phone: {}", phone);
                throw new RuntimeException("Customer not found");
            }
            Customer customer = customerRepo.findByPhone(phone);
            log.info("Fetched customer successfully: {}", customer.getName());
            return customer;
        } catch (Exception e) {
            log.error("Error while fetching customer by phone: {}", phone, e);
            throw e;
        }
    }

    public Customer findByEmail(String email) {
        try {
            log.info("Fetching customer by email: {}", email);
            if (!customerRepo.existsCustomerByEmail(email)) {
                log.warn("Customer not found with email: {}", email);
                throw new RuntimeException("Customer not found");
            }
            Customer customer = customerRepo.findByEmail(email);
            log.info("Fetched customer successfully: {}", customer.getName());
            return customer;
        } catch (Exception e) {
            log.error("Error while fetching customer by email: {}", email, e);
            throw e;
        }
    }

    public Customer createCustomer(Customer customer) {
        try {
            log.info("Creating new customer: {}", customer.getName());
            if (customerRepo.existsCustomerByEmail(customer.getEmail())) {
                log.warn("Email '{}' already exists", customer.getEmail());
                throw new RuntimeException("Email already exists");
            }
            if (customerRepo.existsCustomerByPhone(customer.getPhone())) {
                log.warn("Phone '{}' already exists", customer.getPhone());
                throw new RuntimeException("Phone number already exists");
            }

            Customer saved = customerRepo.save(customer);
            log.info("Customer created successfully: {}", saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Error while creating customer: {}", customer.getName(), e);
            throw e;
        }
    }

    public Customer updateCustomer(Long id, Customer customer) {
        try {
            log.info("Updating customer with ID: {}", id);
            Customer existing = customerRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Customer not found with ID: {}", id);
                        return new RuntimeException("Customer not found");
                    });

            if (customerRepo.existsByEmailAndCustomerIdNot(customer.getEmail(), id)) {
                log.warn("Email '{}' already used by another customer", customer.getEmail());
                throw new RuntimeException("Email is already in use by another customer");
            }
            if (customerRepo.existsByPhoneAndCustomerIdNot(customer.getPhone(), id)) {
                log.warn("Phone '{}' already used by another customer", customer.getPhone());
                throw new RuntimeException("Phone is already in use by another customer");
            }

            existing.setName(customer.getName());
            existing.setEmail(customer.getEmail());
            existing.setPhone(customer.getPhone());
            existing.setAddress(customer.getAddress());

            Customer updated = customerRepo.save(existing);
            log.info("Customer updated successfully: {}", updated.getName());
            return updated;
        } catch (Exception e) {
            log.error("Error while updating customer with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteCustomer(Long id) {
        try {
            log.info("Deleting customer with ID: {}", id);
            Customer customer = customerRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Customer not found with ID: {}", id);
                        return new RuntimeException("Customer not found");
                    });
            customerRepo.delete(customer);
            log.info("Customer deleted successfully: {}", customer.getName());
        } catch (Exception e) {
            log.error("Error while deleting customer with ID: {}", id, e);
            throw e;
        }
    }
}
