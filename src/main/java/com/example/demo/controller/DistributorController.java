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
            List<Distributor> distributors = distributorService.findAllDistributors();
            response.setBody(distributors);
            response.setMessage("Fetched all distributors successfully");
        } catch (Exception e) {
            log.error("Error fetching all distributors", e);
            throw e; // để GlobalExceptionHandler xử lý
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
            Distributor distributor = distributorService.findDistributorById(id);
            response.setBody(distributor);
            response.setMessage("Fetched distributor successfully");
        } catch (Exception e) {
            log.error("Error fetching distributor with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Distributor> createDistributor(@Valid @RequestBody Distributor distributor) {
        log.info("Request to create distributor: {}", distributor.getName());
        Distributor created = distributorService.createDistributor(distributor);

        BaseResponse<Distributor> response = new BaseResponse<>();
        response.setMessage("Distributor created successfully");
        response.setBody(created);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Distributor> updateDistributor(@PathVariable long id, @Valid @RequestBody Distributor distributor) {
        BaseResponse<Distributor> response = new BaseResponse<>();
        try {
            log.info("Request to update distributor with ID: {}", id);
            Distributor updated = distributorService.updateDistributor(id, distributor);
            response.setBody(updated);
            response.setMessage("Distributor updated successfully");
        } catch (Exception e) {
            log.error("Error updating distributor with ID: {}", id, e);
            throw e;
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
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
