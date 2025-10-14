package com.example.demo.repo;

import com.example.demo.dto.Import_InvoiceIF;
import com.example.demo.dto.PromotionIF;
import com.example.demo.model.Import_Invoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Import_InvoiceRepo extends JpaRepository<Import_Invoice,Long> {


    @Query(value = """
    SELECT i.import_id AS importId,
           i.date ,
           d.name AS distributorName,
           i.total_amount AS totalAmount
    FROM import_invoice as i
    JOIN distributor d ON i.distributor_id = d.distributor_id
    """, nativeQuery = true)
    List<Import_InvoiceIF> findAllImport_InvoiceWithRelations();

            @Query(value = """
              SELECT i.import_id AS importId,
                   i.date ,
                   d.name AS distributorName,
                   i.total_amount AS totalAmount
            FROM import_invoice as i
            JOIN distributor d ON i.distributor_id = d.distributor_id
            WHERE i.import_id = :importId
            """, nativeQuery = true)
            Import_InvoiceIF findImport_InvoiceByIdWithRelations(@Param("importId") Long importId);
}
