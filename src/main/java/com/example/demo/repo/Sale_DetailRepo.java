package com.example.demo.repo;

import com.example.demo.dto.Import_DetailIF;
import com.example.demo.dto.Sale_DetailIF;
import com.example.demo.model.Import_Detail;
import com.example.demo.model.Sale_Detail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Sale_DetailRepo extends JpaRepository<Sale_Detail,Long> {
    @Query(value = """
        SELECT s.sale_detail_id as saleDetailId,
            s.sale_id as saleId,
            p.name as productName,
            s.quantity as quantity,
            s.sale_price as price
        FROM sale_detail as s
        JOIN product as p ON s.product_id = p.product_id
        WHERE sale_id = :saleId
    """, nativeQuery = true)
    List<Sale_DetailIF> findAllBySaleId(@Param("saleId") long saleId);

    @Query(value = """
        SELECT *
        FROM sale_detail
        WHERE sale_id = :saleId
    """, nativeQuery = true)
    List<Sale_Detail> findBySaleId(@Param("saleId") long saleId);

    @Query(value = """
        SELECT s.sale_detail_id as saleDetailId,
            s.sale_id as saleId,
            p.name as productName,
            s.quantity as quantity,
            s.sale_price as price
        FROM sale_detail as s
        JOIN product as p ON s.product_id = p.product_id
        WHERE sale_detail_id = :saleDetailId
    """, nativeQuery = true)
    Sale_DetailIF findSaleDetailById(@Param("saleDetailId") long saleDetailId);

    @Modifying
    @Query(value = "DELETE FROM sale_detail WHERE sale_id = :saleId", nativeQuery = true)
    void deleteAllBySaleId(@Param("saleId") long saleId);
}
