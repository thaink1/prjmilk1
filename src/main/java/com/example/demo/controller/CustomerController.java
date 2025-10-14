package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;
    @GetMapping("")
    public BaseResponse<List<Customer>> getAllCustomers() {
        BaseResponse<List<Customer>> response = new BaseResponse<>();
        response.setBody(customerService.getAllCustomers());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    @GetMapping("/email")
    public BaseResponse<Customer> getCustomerByEmail(@RequestParam("email") String email) {
        BaseResponse<Customer> response = new BaseResponse<>();
        response.setBody(customerService.findByEmail(email));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    @GetMapping("/phone")
    public BaseResponse<Customer> getCustomerByPhone(@RequestParam("phone") String phone) {
        BaseResponse<Customer> response = new BaseResponse<>();
        response.setBody(customerService.findByPhone(phone));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    @PostMapping
    public BaseResponse<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        BaseResponse<Customer> response = new BaseResponse<>();
        response.setBody(customerService.createCustomer(customer));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    @PutMapping("/{id}")
    public BaseResponse<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        BaseResponse<Customer> response = new BaseResponse<>();
        response.setBody(customerService.updateCustomer(id, customer));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Deleted Customer with id: " + id + " successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

}
