package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Distributor;
import com.example.demo.service.DistributorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/distributor")
@Slf4j
public class DistributorController {

    private final DistributorService distributorService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public BaseResponse<List<Distributor>> getAllDistributors() {
        BaseResponse<List<Distributor>> response = new BaseResponse<>();
        try {
            log.info("Request to get all distributors");
            response.setBody(distributorService.findAllDistributors());
            response.setMessage("Fetched all distributors successfully");
        } catch (Exception e) {
            log.error("Error fetching all distributors", e);
            response.setMessage("Failed to fetch distributors: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Distributor> getDistributorById(@PathVariable long id) {
        BaseResponse<Distributor> response = new BaseResponse<>();
        try {
            log.info("Request to get distributor by ID: {}", id);
            response.setBody(distributorService.findDistributorById(id));
            response.setMessage("Fetched distributor successfully");
        } catch (Exception e) {
            log.error("Error fetching distributor with ID: {}", id, e);
            response.setMessage("Failed to fetch distributor: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Distributor> createDistributor(@Valid @RequestBody Distributor distributor) {
        BaseResponse<Distributor> response = new BaseResponse<>();
        try {
            log.info("Request to create distributor: {}", distributor.getName());
            response.setBody(distributorService.createDistributor(distributor));
            response.setMessage("Distributor created successfully");
        } catch (Exception e) {
            log.error("Error creating distributor: {}", distributor.getName(), e);
            response.setMessage("Failed to create distributor: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Distributor> updateDistributor(@PathVariable long id, @Valid @RequestBody Distributor distributor) {
        BaseResponse<Distributor> response = new BaseResponse<>();
        try {
            log.info("Request to update distributor with ID: {}", id);
            response.setBody(distributorService.updateDistributor(id, distributor));
            response.setMessage("Distributor updated successfully");
        } catch (Exception e) {
            log.error("Error updating distributor with ID: {}", id, e);
            response.setMessage("Failed to update distributor: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteDistributorById(@PathVariable long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete distributor with ID: {}", id);
            distributorService.deleteDistributor(id);
            response.setMessage("Distributor deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting distributor with ID: {}", id, e);
            response.setMessage("Failed to delete distributor: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
