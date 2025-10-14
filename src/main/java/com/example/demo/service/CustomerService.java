package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repo.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepo customerRepo;
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
    public Customer findByPhone(String phone) {
        if(!customerRepo.existsCustomerByPhone(phone)) {
            throw new RuntimeException("Customer not found");
        }
        return customerRepo.findByPhone(phone);
    }
    public Customer findByEmail(String email) {
        if(!customerRepo.existsCustomerByEmail(email)) {
            throw new RuntimeException("Customer not found");
        }
        return customerRepo.findByEmail(email);
    }
    public Customer createCustomer(Customer customer) {
        if(customerRepo.existsCustomerByEmail(customer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if( customerRepo.existsCustomerByPhone(customer.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        return customerRepo.save(customer);
    }
    public Customer updateCustomer(Long id, Customer customer) {
        Customer customer1 = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customerRepo.existsByEmailAndCustomerIdNot(customer.getEmail(), id)) {
            throw new RuntimeException("Email is already in use by another customer");
        }
        if (customerRepo.existsByPhoneAndCustomerIdNot(customer.getPhone(), id)) {
            throw new RuntimeException("Phone is already in use by another customer");
        }
        customer1.setName(customer.getName());
        customer1.setEmail(customer.getEmail());
        customer1.setPhone(customer.getPhone());
        customer1.setAddress(customer.getAddress());
        customerRepo.save(customer1);
        return customer;
    }
    public void deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepo.delete(customer);
    }
}
