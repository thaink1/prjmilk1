package com.example.demo.repo;

import com.example.demo.dto.Import_InvoiceIF;
import com.example.demo.dto.Sale_InvoiceIF;
import com.example.demo.model.Sale_Invoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Sale_InvoiceRepo extends CrudRepository<Sale_Invoice, Long> {
    @Query(value = """
    SELECT s.sale_id AS saleId,
           s.date ,
           c.name AS customerName,
           s.total_amount AS totalAmount
    FROM sale_invoice as s
    JOIN customer c ON s.customer_id = c.customer_id
    """, nativeQuery = true)
    List<Sale_InvoiceIF> findAllSale_InvoiceWithRelations();

    @Query(value = """
    SELECT s.sale_id AS saleId,
           s.date ,
           c.name AS customerName,
           s.total_amount AS totalAmount
    FROM sale_invoice as s
    JOIN customer c ON s.customer_id = c.customer_id
    WHERE s.sale_id = :saleId
    """, nativeQuery = true)
    Sale_InvoiceIF finSale_InvoiceBySaleIdWithRelations(@Param("saleId") Long saleId);
}
