package com.example.demo.repo;

import com.example.demo.dto.Import_DetailIF;
import com.example.demo.model.Import_Detail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Import_DetailRepo extends JpaRepository<Import_Detail,Long> {
    @Query(value = """
        SELECT i.import_detail_id as ipDetailId,
            i.import_id as importId,
            p.name as productName,
            i.quantity as quantity,
            i.import_price as price
        FROM import_detail as i
        JOIN product as p ON i.product_id = p.product_id
        WHERE import_id = :importId
    """, nativeQuery = true)
    List<Import_DetailIF> findAllByImportId(@Param("importId") long importId);

    @Query(value = """
        SELECT *
        FROM import_detail
        WHERE import_id = :importId
    """, nativeQuery = true)
    List<Import_Detail> findByImportId(@Param("importId") long importId);

    @Query(value = """
        SELECT i.import_detail_id as ipDetailId,
            i.import_id as importId,
            p.name as productName,
            i.quantity as quantity,
            i.import_price as price
        FROM import_detail as i
        JOIN product as p ON i.product_id = p.product_id
        WHERE i.import_detail_id = :ipDetailId
    """, nativeQuery = true)
    Import_DetailIF findImportDetailById(@Param("ipDetailId") long ipDetailId);

    @Modifying
    @Query(value = "DELETE FROM import_detail WHERE import_id = :importId", nativeQuery = true)
    void deleteAllByImportId(@Param("importId") long importId);

}
