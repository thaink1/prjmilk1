package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.PromotionResponse;
import com.example.demo.model.Promotion;
import com.example.demo.service.PromotionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {
    private PromotionService promotionService;
    ObjectMapper objectMapper = new ObjectMapper();
    @GetMapping("")
    public BaseResponse<List<PromotionResponse>> findAll(){
        BaseResponse<List<PromotionResponse>> response = new BaseResponse<>();
        response.setBody(promotionService.getAllPromotions());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<PromotionResponse> createPromotion(@Valid  @RequestBody Promotion promotion){
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(promotionService.createPromotion(promotion));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<PromotionResponse> getPromotionById(@PathVariable Long id){
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(promotionService.getPromotionById(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<PromotionResponse> updatePromotion(@PathVariable Long id, @Valid  @RequestBody Promotion promotion){
        BaseResponse<PromotionResponse> response = new BaseResponse<>();
        response.setBody(promotionService.updatePromotion(id, promotion));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deletePromotion(@PathVariable Long id){
        promotionService.deletePromotion(id);
        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Delete promotion successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

}
