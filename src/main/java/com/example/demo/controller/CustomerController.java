package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.CustomerSearch;
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
            List<Customer> customers = customerService.getAllCustomers();
            response.setBody(customers);
            response.setMessage("Fetched all customers successfully");
        } catch (Exception e) {
            log.error("Error fetching all customers", e);
            throw e;
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
            Customer customer = customerService.findByEmail(email);
            response.setBody(customer);
            response.setMessage("Fetched customer successfully");
        } catch (Exception e) {
            log.error("Error fetching customer by email: {}", email, e);
            throw e;
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
            Customer customer = customerService.findByPhone(phone);
            response.setBody(customer);
            response.setMessage("Fetched customer successfully");
        } catch (Exception e) {
            log.error("Error fetching customer by phone: {}", phone, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping
    public BaseResponse<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Request to create customer: {}", customer.getEmail());
        Customer created = customerService.createCustomer(customer);

        BaseResponse<Customer> response = new BaseResponse<>();
        response.setMessage("Customer created successfully");
        response.setBody(created);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        BaseResponse<Customer> response = new BaseResponse<>();
        try {
            log.info("Request to update customer with ID: {}", id);
            Customer updated = customerService.updateCustomer(id, customer);
            response.setBody(updated);
            response.setMessage("Customer updated successfully");
        } catch (Exception e) {
            log.error("Error updating customer with ID: {}", id, e);
            throw e;
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
            response.setMessage("Deleted customer with ID: " + id + " successfully");
        } catch (Exception e) {
            log.error("Error deleting customer with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("/search")
    public BaseResponse<List<Customer>> searchCustomers(@RequestBody CustomerSearch request) {
        BaseResponse<List<Customer>> response = new BaseResponse<>();
        try{
            log.info("Request to search customers {}", request);
            List<Customer> customers = customerService.searchCustomers(request);
            if (customers.size() == 0 ){
                response.setMessage("No customers found");
                response.setBody(customers);
            }else {
                response.setBody(customers);
                response.setMessage("Fetched customers successfully");
            }
        }catch (Exception e){
            log.error("Error fetching customers {}", request, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
