package com.example.demo.repo;

import com.example.demo.dto.ProductIF;
import com.example.demo.dto.PromotionIF;
import com.example.demo.model.Promotion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepo extends JpaRepository<Promotion, Long> {
    Optional<Promotion> getPromotionByPromotionId(long id);
    void deleteByProductId(long id);

    @Query("""
       SELECT COUNT(p) > 0
       FROM Promotion p
       WHERE p.productId = :productId
         AND (p.startDate <= :endDate AND p.endDate >= :startDate)
       """)
    boolean existsOverlappingPromotion(@Param("productId") Long productId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT pr.promotion_id AS promotionId,
           pr.name AS name,
           pr.value AS value,
           pr.start_date AS startDate,
           pr.end_date AS endDate,
           p.name AS productName
    FROM promotion as pr
    JOIN product p ON pr.product_id = p.product_id
    """, nativeQuery = true)
    List<PromotionIF> findAllPromotionWithRelations();

    @Query(value = """
     SELECT pr.promotion_id AS promotionId,
           pr.name AS name,
           pr.value AS value,
           pr.start_date AS startDate,
           pr.end_date AS endDate,
           p.name AS productName
    FROM promotion as pr
    JOIN product p ON pr.product_id = p.product_id
    WHERE pr.promotion_id = :promotionId
    """, nativeQuery = true)
    PromotionIF findPromotionByIdWithRelations(@Param("promotionId") Long promotionId);

    @Query("""
       SELECT COUNT(p) > 0
       FROM Promotion p
       WHERE p.productId = :productId
         AND p.promotionId <> :promotionId
         AND (p.startDate <= :endDate AND p.endDate >= :startDate)
       """)
    boolean existsOverlappingPromotionForUpdate(@Param("productId") Long productId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("promotionId") Long promotionId);


}
