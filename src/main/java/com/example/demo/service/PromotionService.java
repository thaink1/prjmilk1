package com.example.demo.service;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.PromotionIF;
import com.example.demo.dto.PromotionResponse;
import com.example.demo.model.Product;
import com.example.demo.model.Promotion;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.PromotionRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PromotionService {
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final PromotionRepo promotionRepo;

    public List<PromotionResponse> getAllPromotions(){
        List<PromotionIF> results = promotionRepo.findAllPromotionWithRelations();
        List<PromotionResponse> response = objectMapper
                .convertValue(results, new TypeReference<List<PromotionResponse>>() {});
        return response;
    }

    public PromotionResponse getPromotionById(long id){
        Promotion promotion = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        PromotionIF result = promotionRepo.findPromotionByIdWithRelations(promotion.getPromotionId());
        PromotionResponse promotionResponse = objectMapper.convertValue(result, new TypeReference<PromotionResponse>() {});
        return promotionResponse;
    }

    public PromotionResponse createPromotion(Promotion promotion){
        long productId = promotion.getProductId();
        if(!productRepo.existsById(productId)){
            throw new RuntimeException("Product not found");
        }
        boolean exists = promotionRepo.existsOverlappingPromotion(promotion.getProductId(),
                                                                  promotion.getStartDate(),
                                                                  promotion.getEndDate());
        if(exists){
            throw new RuntimeException("This product already has a promotion in the given time range");
        }
        if (!promotion.getEndDate().isAfter(promotion.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }
        promotionRepo.save(promotion);
        PromotionIF result = promotionRepo.findPromotionByIdWithRelations(promotion.getPromotionId());
        PromotionResponse promotionResponse = objectMapper.convertValue(result, new TypeReference<PromotionResponse>() {});
        return promotionResponse;
    }

    public PromotionResponse updatePromotion(long id, Promotion promotion) {
        Promotion existing = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        long productId = promotion.getProductId();
        if (!productRepo.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }

        if (!promotion.getEndDate().isAfter(promotion.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        boolean overlap = promotionRepo.existsOverlappingPromotionForUpdate(
                productId,
                promotion.getStartDate(),
                promotion.getEndDate(),
                id
        );

        if (overlap) {
            throw new RuntimeException("This product already has a promotion in the given time range");
        }

        existing.setName(promotion.getName());
        existing.setValue(promotion.getValue());
        existing.setStartDate(promotion.getStartDate());
        existing.setEndDate(promotion.getEndDate());
        existing.setProductId(productId);

        promotionRepo.save(existing);
        PromotionIF result = promotionRepo.findPromotionByIdWithRelations(id);
        PromotionResponse promotionResponse = objectMapper.convertValue(result, new TypeReference<PromotionResponse>() {});
        return promotionResponse;
    }

    public void deletePromotion(long id){
        Promotion promotion = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotionRepo.deleteById(id);
    }

}
