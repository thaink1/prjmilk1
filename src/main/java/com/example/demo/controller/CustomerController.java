package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("")
    public BaseResponse<List<Customer>> getAllCustomers() {
        BaseResponse<List<Customer>> response = new BaseResponse<>();
        try {
            log.info("Request to get all customers");
            response.setBody(customerService.getAllCustomers());
            response.setMessage("Fetched all customers successfully");
        } catch (Exception e) {
            log.error("Error fetching all customers", e);
            response.setMessage("Failed to fetch customers: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/email")
    public BaseResponse<Customer> getCustomerByEmail(@RequestParam("email") String email) {
        BaseResponse<Customer> response = new BaseResponse<>();
        try {
            log.info("Request to get customer by email: {}", email);
            response.setBody(customerService.findByEmail(email));
            response.setMessage("Fetched customer successfully");
        } catch (Exception e) {
            log.error("Error fetching customer by email: {}", email, e);
            response.setMessage("Failed to fetch customer: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/phone")
    public BaseResponse<Customer> getCustomerByPhone(@RequestParam("phone") String phone) {
        BaseResponse<Customer> response = new BaseResponse<>();
        try {
            log.info("Request to get customer by phone: {}", phone);
            response.setBody(customerService.findByPhone(phone));
            response.setMessage("Fetched customer successfully");
        } catch (Exception e) {
            log.error("Error fetching customer by phone: {}", phone, e);
            response.setMessage("Failed to fetch customer: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping
    public BaseResponse<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        BaseResponse<Customer> response = new BaseResponse<>();
        try {
            log.info("Request to create customer: {}", customer.getEmail());
            response.setBody(customerService.createCustomer(customer));
            response.setMessage("Customer created successfully");
        } catch (Exception e) {
            log.error("Error creating customer: {}", customer.getEmail(), e);
            response.setMessage("Failed to create customer: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        BaseResponse<Customer> response = new BaseResponse<>();
        try {
            log.info("Request to update customer with ID: {}", id);
            response.setBody(customerService.updateCustomer(id, customer));
            response.setMessage("Customer updated successfully");
        } catch (Exception e) {
            log.error("Error updating customer with ID: {}", id, e);
            response.setMessage("Failed to update customer: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteCustomer(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete customer with ID: {}", id);
            customerService.deleteCustomer(id);
            response.setMessage("Deleted Customer with id: " + id + " successfully");
        } catch (Exception e) {
            log.error("Error deleting customer with ID: {}", id, e);
            response.setMessage("Failed to delete customer: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
