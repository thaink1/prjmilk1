package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.PromotionResponse;
import com.example.demo.model.Promotion;
import com.example.demo.service.PromotionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/promotion")
@Slf4j
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("")
    public BaseResponse<List<PromotionResponse>> findAll() {
        BaseResponse<List<PromotionResponse>> response = new BaseResponse<>();
        try {
            log.info("Fetching all promotions");
            response.setBody(promotionService.getAllPromotions());
            response.setMessage("Fetched all promotions successfully");
        } catch (Exception e) {
            log.error("Error fetching promotions", e);
            response.setMessage("Failed to fetch promotions: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<PromotionResponse> getPromotionById(@PathVariable Long id) {
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        try {
            log.info("Fetching promotion with ID: {}", id);
            response.setBody(promotionService.getPromotionById(id));
            response.setMessage("Fetched promotion successfully");
        } catch (Exception e) {
            log.error("Error fetching promotion with ID: {}", id, e);
            response.setMessage("Failed to fetch promotion: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<PromotionResponse> createPromotion(@Valid @RequestBody Promotion promotion) {
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        try {
            log.info("Creating new promotion: {}", promotion.getName());
            response.setBody(promotionService.createPromotion(promotion));
            response.setMessage("Promotion created successfully");
        } catch (Exception e) {
            log.error("Error creating promotion", e);
            response.setMessage("Failed to create promotion: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<PromotionResponse> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody Promotion promotion) {
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        try {
            log.info("Updating promotion ID: {}", id);
            response.setBody(promotionService.updatePromotion(id, promotion));
            response.setMessage("Promotion updated successfully");
        } catch (Exception e) {
            log.error("Error updating promotion ID: {}", id, e);
            response.setMessage("Failed to update promotion: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deletePromotion(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Deleting promotion ID: {}", id);
            promotionService.deletePromotion(id);
            response.setMessage("Promotion deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting promotion ID: {}", id, e);
            response.setMessage("Failed to delete promotion: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
