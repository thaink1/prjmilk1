package com.example.demo.repo;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT p.product_id, p.name, p.capacity_ml, p.weight_gr, 
               p.unit_price, p.stock_quantity, p.mfg_date, p.exp_date,
               c.name AS category_name, b.name, d.name AS distributor_name
        FROM product p
        JOIN category c ON p.category_id = c.category_id
        JOIN brand b ON p.brand_id = b.brand_id
        JOIN distributor d ON p.distributor_id = d.distributor_id
        """, nativeQuery = true)
    List<Object[]> findAllProductsWithRelations();


    @Query(value = """
        SELECT i.image_id, i.url, i.is_primary, i.created_at
        FROM product_image i
        WHERE i.product_id = :productId
        """, nativeQuery = true)
    List<Object[]> findImagesByProductId(@Param("productId") Long productId);
}
