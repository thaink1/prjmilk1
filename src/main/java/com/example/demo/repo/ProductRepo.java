package com.example.demo.repo;

import com.example.demo.dto.ProductIF;
import com.example.demo.dto.ProductImageIF;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = """
    SELECT p.product_id AS productId,
           p.name AS productName,
           p.capacity_ml AS capacity,
           p.weight_gr AS weightGr,
           p.unit_price AS price,
           CASE
            WHEN pr.promotion_id IS NOT NULL
                AND CURRENT_DATE BETWEEN pr.start_date AND pr.end_date
            THEN p.unit_price * (1 - pr.value / 100)
                ELSE p.unit_price
           END AS finalPrice,
           p.stock_quantity AS stockQuantity,
           p.mfg_date AS createdDate,
           p.exp_date AS expiredDate,
           c.name AS categoryName,
           b.name AS brandName,
           d.name AS distributorName
    FROM product p
    JOIN category c ON p.category_id = c.category_id
    JOIN brand b ON p.brand_id = b.brand_id
    JOIN distributor d ON p.distributor_id = d.distributor_id
    LEFT JOIN promotion pr
    ON pr.product_id = p.product_id
    AND CURRENT_DATE BETWEEN pr.start_date AND pr.end_date
    """, nativeQuery = true)
    List<ProductIF> findAllProductsWithRelations();

    @Query(value = """
    SELECT i.image_id AS imageId,
           i.url AS url,
           i.is_primary AS isPrimary,
           i.created_at AS createdAt
    FROM product_image i
    WHERE i.product_id = :productId
    """, nativeQuery = true)
    List<ProductImageIF> findImagesByProductId(@Param("productId") Long productId);

    @Query(value = """
    SELECT p.product_id AS productId,
           p.name AS productName,
           p.capacity_ml AS capacity,
           p.weight_gr AS weightGr,
           p.unit_price AS price,
           CASE
            WHEN pr.promotion_id IS NOT NULL
                AND CURRENT_DATE BETWEEN pr.start_date AND pr.end_date
            THEN p.unit_price * (1 - pr.value / 100)
                ELSE p.unit_price
           END AS finalPrice,
           p.stock_quantity AS stockQuantity,
           p.mfg_date AS createdDate,
           p.exp_date AS expiredDate,
           c.name AS categoryName,
           b.name AS brandName,
           d.name AS distributorName
    FROM product p
    JOIN category c ON p.category_id = c.category_id
    JOIN brand b ON p.brand_id = b.brand_id
    JOIN distributor d ON p.distributor_id = d.distributor_id
    LEFT JOIN promotion pr
    ON pr.product_id = p.product_id
    AND CURRENT_DATE BETWEEN pr.start_date AND pr.end_date
    WHERE p.product_id = :productId
    """, nativeQuery = true)
    ProductIF findProductByIdWithRelations(@Param("productId") Long productId);


}
