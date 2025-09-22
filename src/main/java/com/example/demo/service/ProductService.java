package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repo.ProductRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    @PersistenceContext
    private EntityManager entityManager;


    public List<ProductResponse> getAllProducts() {
        List<Object[]> results = productRepo.findAllProductsWithRelations();
        List<ProductResponse> responses = new ArrayList<>();

        for (Object[] row : results) {
            Long productId = ((Number) row[0]).longValue();

            List<Object[]> imageRows = productRepo.findImagesByProductId(productId);

            ProductResponse res = productMapper.toProductResponse(row, imageRows);
            responses.add(res);
        }
        return responses;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        if (request.getCreatedDate() != null && request.getExpiredDate() != null) {
            if (request.getCreatedDate().after(request.getExpiredDate())) {
                throw new IllegalArgumentException("Created date cannot be after expired date");
            }
        }
        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException("Price cannot be less than 0");
        }
        if (request.getWeightGr() < 0) {
            throw new IllegalArgumentException("Weight gram cannot be less than 0");
        }
        if (request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be less than 0");
        }
        boolean existsCategory = entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM category WHERE category_id = :id")
                .setParameter("id", request.getCategoryId())
                .getSingleResult()
                .toString()
                .equals("1");

        if (!existsCategory) {
            throw new IllegalArgumentException("Category does not exist");
        }
        boolean existsBrand = entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM brand WHERE brand_id = :id")
                .setParameter("id", request.getBrandId())
                .getSingleResult()
                .toString()
                .equals("1");

        if (!existsBrand) {
            throw new IllegalArgumentException("Brand does not exist");
        }
        boolean existsDistributor = entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM distributor WHERE distributor_id = :id")
                .setParameter("id", request.getDistributorId())
                .getSingleResult()
                .toString()
                .equals("1");

        if (!existsDistributor) {
            throw new IllegalArgumentException("Distributor does not exist");
        }
        Long productId = ((Number) entityManager.createNativeQuery("""
            INSERT INTO product (name, capacity_ml, weight_gr, unit_price, stock_quantity,
                                 mfg_date, exp_date, category_id, brand_id, distributor_id)
            VALUES (:name, :capacity, :weightGr, :price, :stockQuantity,
                    :mfgDate, :expDate, :categoryId, :brandId, :distributorId)
            RETURNING product_id
            """)
                .setParameter("name", request.getProductName())
                .setParameter("capacity", request.getCapacity())
                .setParameter("weightGr", request.getWeightGr())
                .setParameter("price", request.getPrice())
                .setParameter("stockQuantity", request.getStockQuantity())
                .setParameter("mfgDate", request.getCreatedDate())
                .setParameter("expDate", request.getExpiredDate())
                .setParameter("categoryId", request.getCategoryId())
                .setParameter("brandId", request.getBrandId())
                .setParameter("distributorId", request.getDistributorId())
                .getSingleResult()).longValue();

        if (request.getImages() != null) {
            for (var img : request.getImages()) {
                entityManager.createNativeQuery("""
                    INSERT INTO product_image (product_id, url, is_primary, created_at)
                    VALUES (:productId, :url, :isPrimary, NOW())
                    """)
                        .setParameter("productId", productId)
                        .setParameter("url", img.getUrl())
                        .setParameter("isPrimary", img.getIsPrimary())
                        .executeUpdate();
            }
        }

        // Query lại để trả về
        Object[] row = productRepo.findAllProductsWithRelations()
                .stream()
                .filter(r -> ((Number) r[0]).longValue() == productId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found after insert"));

        List<Object[]> imageRows = productRepo.findImagesByProductId(productId);
        return productMapper.toProductResponse(row, imageRows);
    }
}
