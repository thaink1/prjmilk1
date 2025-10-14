package com.example.demo.service;

import com.example.demo.dto.Import_DetailIF;
import com.example.demo.dto.Import_DetailResponse;
import com.example.demo.dto.Import_InvoiceIF;
import com.example.demo.dto.Import_InvoiceResponse;
import com.example.demo.model.Import_Detail;
import com.example.demo.model.Import_Invoice;
import com.example.demo.model.Inventory;
import com.example.demo.model.Product;
import com.example.demo.repo.Import_DetailRepo;
import com.example.demo.repo.Import_InvoiceRepo;
import com.example.demo.repo.InventoryRepo;
import com.example.demo.repo.ProductRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Import_DetailService {
    private Import_DetailRepo import_DetailRepo;
    private Import_InvoiceRepo import_InvoiceRepo;
    private ProductRepo productRepo;
    private ObjectMapper objectMapper;
    private InventoryRepo inventoryRepo;

    public List<Import_DetailResponse> getDetailByImportId(Long importId) {
        List<Import_DetailIF> results = import_DetailRepo.findAllByImportId(importId);
        List<Import_DetailResponse> response = objectMapper
                .convertValue(results, new TypeReference<List<Import_DetailResponse>>() {});
        return response;
    }

    public Import_DetailResponse getDetailById(Long id){
        Import_Detail importDetail = import_DetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("DetailsId not found"));
        Import_DetailIF result = import_DetailRepo.findImportDetailById(id);
        Import_DetailResponse response = objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
        return response;
    }

    public Import_DetailResponse createDetail(Import_Detail importDetail) {
        Import_Invoice importInvoice = import_InvoiceRepo.findById(importDetail.getImportId())
                .orElseThrow(() -> new RuntimeException("InvoiceId not found"));
        Product product = productRepo.findById(importDetail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        import_DetailRepo.save(importDetail);

        int qty = importDetail.getQuantity();
        product.setStockQuantity(product.getStockQuantity() + qty);
        productRepo.save(product);

        BigDecimal oldTotalAmount = importInvoice.getTotalAmount();
        BigDecimal detailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(qty));
        BigDecimal newTotal = oldTotalAmount.add(detailTotal);

        importInvoice.setTotalAmount(newTotal);
        import_InvoiceRepo.save(importInvoice);

        Inventory inventory = new Inventory();
        inventory.setProductId(importDetail.getProductId());
        inventory.setChangeQuantity(importDetail.getQuantity());
        inventory.setDate(importInvoice.getDate().atStartOfDay());
        inventory.setReason("Import Product");
        inventoryRepo.save(inventory);


        Import_DetailIF result = import_DetailRepo.findImportDetailById(importDetail.getIpDetailId());
        Import_DetailResponse response = objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
        return response;
    }


    public Import_DetailResponse updateDetail(long id, Import_Detail importDetail) {
        Import_Detail existingDetail = import_DetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("DetailsId not found"));

        Import_Invoice importInvoice = import_InvoiceRepo.findById(existingDetail.getImportId())
                .orElseThrow(() -> new RuntimeException("InvoiceId not found"));

        Product product = productRepo.findById(existingDetail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int oldQty = existingDetail.getQuantity();
        int newQty = importDetail.getQuantity();
        product.setStockQuantity(product.getStockQuantity() - oldQty + newQty);
        productRepo.save(product);

        BigDecimal oldDetailTotal = existingDetail.getPrice().multiply(BigDecimal.valueOf(oldQty));
        BigDecimal newDetailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(newQty));

        BigDecimal newInvoiceTotal = importInvoice.getTotalAmount()
                .subtract(oldDetailTotal)
                .add(newDetailTotal);
        importInvoice.setTotalAmount(newInvoiceTotal);
        import_InvoiceRepo.save(importInvoice);

        importDetail.setIpDetailId(id);
        import_DetailRepo.save(importDetail);

        Inventory inventory = new Inventory();
        inventory.setProductId(importDetail.getProductId());
        inventory.setChangeQuantity(newQty - oldQty);
        inventory.setReason("Change Import Product");
        inventory.setDate(importInvoice.getDate().atStartOfDay());
        inventoryRepo.save(inventory);


        Import_DetailIF result = import_DetailRepo.findImportDetailById(id);
        return objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
    }


    public void deleteDetailById(long id) {
        Import_Detail importDetail = import_DetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("DetailsId not found"));

        Import_Invoice importInvoice = import_InvoiceRepo.findById(importDetail.getImportId())
                .orElseThrow(() -> new RuntimeException("InvoiceId not found"));

        Product product = productRepo.findById(importDetail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(product.getStockQuantity() - importDetail.getQuantity());
        productRepo.save(product);

        BigDecimal detailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(importDetail.getQuantity()));
        BigDecimal newInvoiceTotal = importInvoice.getTotalAmount().subtract(detailTotal);
        importInvoice.setTotalAmount(newInvoiceTotal);
        import_InvoiceRepo.save(importInvoice);

        Inventory inventory = new Inventory();
        inventory.setProductId(importDetail.getProductId());
        inventory.setChangeQuantity(-importDetail.getQuantity());
        inventory.setDate(importInvoice.getDate().atStartOfDay());
        inventory.setReason("Cancel Import");
        inventoryRepo.save(inventory);

        import_DetailRepo.delete(importDetail);
    }


}
