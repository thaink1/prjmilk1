package com.example.demo.service;

import com.example.demo.dto.PromotionIF;
import com.example.demo.dto.PromotionResponse;
import com.example.demo.model.Promotion;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.PromotionRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PromotionService {

    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final PromotionRepo promotionRepo;

    public List<PromotionResponse> getAllPromotions() {
        try {
            log.info("Fetching all promotions...");
            List<PromotionIF> results = promotionRepo.findAllPromotionWithRelations();
            List<PromotionResponse> response = objectMapper
                    .convertValue(results, new TypeReference<>() {});
            log.info("Fetched {} promotions successfully", response.size());
            return response;
        } catch (Exception e) {
            log.error("Error while fetching promotions", e);
            throw e;
        }
    }

    public PromotionResponse getPromotionById(long id) {
        try {
            log.info("Fetching promotion with ID: {}", id);
            Promotion promotion = promotionRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Promotion not found with ID: {}", id);
                        return new RuntimeException("Promotion not found");
                    });

            PromotionIF result = promotionRepo.findPromotionByIdWithRelations(promotion.getPromotionId());
            PromotionResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            log.info("Fetched promotion successfully with ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error fetching promotion with ID: {}", id, e);
            throw e;
        }
    }

    public PromotionResponse createPromotion(Promotion promotion) {
        try {
            log.info("Creating new promotion for product ID: {}", promotion.getProductId());

            long productId = promotion.getProductId();
            if (!productRepo.existsById(productId)) {
                log.warn("Product not found with ID: {}", productId);
                throw new RuntimeException("Product not found");
            }

            if (!promotion.getEndDate().isAfter(promotion.getStartDate())) {
                throw new RuntimeException("End date must be after start date");
            }

            boolean exists = promotionRepo.existsOverlappingPromotion(
                    promotion.getProductId(),
                    promotion.getStartDate(),
                    promotion.getEndDate()
            );
            if (exists) {
                log.warn("Overlapping promotion detected for product ID: {}", promotion.getProductId());
                throw new RuntimeException("This product already has a promotion in the given time range");
            }

            promotionRepo.save(promotion);
            log.info("Promotion created successfully with ID: {}", promotion.getPromotionId());

            PromotionIF result = promotionRepo.findPromotionByIdWithRelations(promotion.getPromotionId());
            PromotionResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            return response;
        } catch (Exception e) {
            log.error("Error creating promotion for product ID: {}", promotion.getProductId(), e);
            throw e;
        }
    }

    public PromotionResponse updatePromotion(long id, Promotion promotion) {
        try {
            log.info("Updating promotion with ID: {}", id);
            Promotion existing = promotionRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Promotion not found with ID: {}", id);
                        return new RuntimeException("Promotion not found");
                    });

            long productId = promotion.getProductId();
            if (!productRepo.existsById(productId)) {
                log.warn("Product not found with ID: {}", productId);
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
                log.warn("Overlapping promotion detected for product ID: {} when updating ID: {}", productId, id);
                throw new RuntimeException("This product already has a promotion in the given time range");
            }

            existing.setName(promotion.getName());
            existing.setValue(promotion.getValue());
            existing.setStartDate(promotion.getStartDate());
            existing.setEndDate(promotion.getEndDate());
            existing.setProductId(productId);

            promotionRepo.save(existing);
            log.info("Promotion updated successfully with ID: {}", id);

            PromotionIF result = promotionRepo.findPromotionByIdWithRelations(id);
            PromotionResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            return response;
        } catch (Exception e) {
            log.error("Error updating promotion with ID: {}", id, e);
            throw e;
        }
    }

    public void deletePromotion(long id) {
        try {
            log.info("Deleting promotion with ID: {}", id);
            Promotion promotion = promotionRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Promotion not found with ID: {}", id);
                        return new RuntimeException("Promotion not found");
                    });
            promotionRepo.deleteById(id);
            log.info("Promotion deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting promotion with ID: {}", id, e);
            throw e;
        }
    }
}
