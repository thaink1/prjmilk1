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
        log.info("Fetching all promotions");
        List<PromotionResponse> promotions = promotionService.getAllPromotions();

        BaseResponse<List<PromotionResponse>> response = new BaseResponse<>();
        response.setBody(promotions);
        response.setMessage("Fetched all promotions successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<PromotionResponse> getPromotionById(@PathVariable Long id) {
        log.info("Fetching promotion with ID: {}", id);
        PromotionResponse promotion = promotionService.getPromotionById(id);

        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(promotion);
        response.setMessage("Fetched promotion successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<PromotionResponse> createPromotion(@Valid @RequestBody Promotion promotion) {
        log.info("Creating new promotion: {}", promotion.getName());
        PromotionResponse created = promotionService.createPromotion(promotion);

        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(created);
        response.setMessage("Promotion created successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<PromotionResponse> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody Promotion promotion) {
        log.info("Updating promotion ID: {}", id);
        PromotionResponse updated = promotionService.updatePromotion(id, promotion);

        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(updated);
        response.setMessage("Promotion updated successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deletePromotion(@PathVariable Long id) {
        log.info("Deleting promotion ID: {}", id);
        promotionService.deletePromotion(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Promotion deleted successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
