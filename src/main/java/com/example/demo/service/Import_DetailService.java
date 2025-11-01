package com.example.demo.service;

import com.example.demo.dto.Import_DetailIF;
import com.example.demo.dto.Import_DetailResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class Import_DetailService {

    private final Import_DetailRepo import_DetailRepo;
    private final Import_InvoiceRepo import_InvoiceRepo;
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final InventoryRepo inventoryRepo;

    public List<Import_DetailResponse> getDetailByImportId(Long importId) {
        try {
            log.info("Fetching import details by importId: {}", importId);
            List<Import_DetailIF> results = import_DetailRepo.findAllByImportId(importId);
            List<Import_DetailResponse> response = objectMapper.convertValue(
                    results, new TypeReference<List<Import_DetailResponse>>() {});
            log.info("Fetched {} import details for importId {}", response.size(), importId);
            return response;
        } catch (Exception e) {
            log.error("Error fetching details for importId: {}", importId, e);
            throw e;
        }
    }

    public Import_DetailResponse getDetailById(Long id) {
        try {
            log.info("Fetching import detail with ID: {}", id);
            import_DetailRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import detail not found with ID: {}", id);
                        return new RuntimeException("DetailsId not found");
                    });
            Import_DetailIF result = import_DetailRepo.findImportDetailById(id);
            Import_DetailResponse response = objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
            log.info("Fetched import detail successfully with ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error fetching import detail with ID: {}", id, e);
            throw e;
        }
    }

    public Import_DetailResponse createDetail(Import_Detail importDetail) {
        try {
            log.info("Creating import detail for invoice ID: {}", importDetail.getImportId());

            Import_Invoice importInvoice = import_InvoiceRepo.findById(importDetail.getImportId())
                    .orElseThrow(() -> {
                        log.warn("Invoice not found with ID: {}", importDetail.getImportId());
                        return new RuntimeException("InvoiceId not found");
                    });

            Product product = productRepo.findById(importDetail.getProductId())
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", importDetail.getProductId());
                        return new RuntimeException("Product not found");
                    });

            import_DetailRepo.save(importDetail);
            log.debug("Saved import detail for product {}", product.getProductName());

            int qty = importDetail.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + qty);
            productRepo.save(product);
            log.info("Updated stock quantity for product {} to {}", product.getProductName(), product.getStockQuantity());

            BigDecimal detailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(qty));
            importInvoice.setTotalAmount(importInvoice.getTotalAmount().add(detailTotal));
            import_InvoiceRepo.save(importInvoice);
            log.info("Updated invoice total to {}", importInvoice.getTotalAmount());

            Inventory inventory = new Inventory();
            inventory.setProductId(importDetail.getProductId());
            inventory.setChangeQuantity(importDetail.getQuantity());
            inventory.setDate(importInvoice.getDate().atStartOfDay());
            inventory.setReason("Import Product");
            inventoryRepo.save(inventory);

            log.info("Recorded inventory change for import product ID {}", importDetail.getProductId());

            Import_DetailIF result = import_DetailRepo.findImportDetailById(importDetail.getIpDetailId());
            Import_DetailResponse response = objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
            log.info("Created import detail successfully with ID: {}", response.getIpDetailId());
            return response;
        } catch (Exception e) {
            log.error("Error creating import detail for invoice ID: {}", importDetail.getImportId(), e);
            throw e;
        }
    }

    public Import_DetailResponse updateDetail(long id, Import_Detail importDetail) {
        try {
            log.info("Updating import detail with ID: {}", id);
            Import_Detail existingDetail = import_DetailRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import detail not found with ID: {}", id);
                        return new RuntimeException("DetailsId not found");
                    });

            Import_Invoice importInvoice = import_InvoiceRepo.findById(existingDetail.getImportId())
                    .orElseThrow(() -> new RuntimeException("InvoiceId not found"));

            Product product = productRepo.findById(existingDetail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int oldQty = existingDetail.getQuantity();
            int newQty = importDetail.getQuantity();
            product.setStockQuantity(product.getStockQuantity() - oldQty + newQty);
            productRepo.save(product);
            log.info("Adjusted stock quantity for product {} to {}", product.getProductName(), product.getStockQuantity());

            BigDecimal oldDetailTotal = existingDetail.getPrice().multiply(BigDecimal.valueOf(oldQty));
            BigDecimal newDetailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(newQty));

            BigDecimal newInvoiceTotal = importInvoice.getTotalAmount()
                    .subtract(oldDetailTotal)
                    .add(newDetailTotal);
            importInvoice.setTotalAmount(newInvoiceTotal);
            import_InvoiceRepo.save(importInvoice);
            log.info("Updated invoice total to {}", newInvoiceTotal);

            importDetail.setIpDetailId(id);
            import_DetailRepo.save(importDetail);
            log.info("Updated import detail successfully for ID {}", id);


            Inventory inventory = new Inventory();
            inventory.setProductId(importDetail.getProductId());
            inventory.setChangeQuantity(newQty - oldQty);
            inventory.setReason("Change Import Product");
            inventory.setDate(importInvoice.getDate().atStartOfDay());
            inventoryRepo.save(inventory);

            Import_DetailIF result = import_DetailRepo.findImportDetailById(id);
            return objectMapper.convertValue(result, new TypeReference<Import_DetailResponse>() {});
        } catch (Exception e) {
            log.error("Error updating import detail with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteDetailById(long id) {
        try {
            log.info("Deleting import detail with ID: {}", id);
            Import_Detail importDetail = import_DetailRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import detail not found with ID: {}", id);
                        return new RuntimeException("DetailsId not found");
                    });

            Import_Invoice importInvoice = import_InvoiceRepo.findById(importDetail.getImportId())
                    .orElseThrow(() -> new RuntimeException("InvoiceId not found"));

            Product product = productRepo.findById(importDetail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setStockQuantity(product.getStockQuantity() - importDetail.getQuantity());
            productRepo.save(product);
            log.info("Reduced stock quantity for product {} to {}", product.getProductName(), product.getStockQuantity());

            BigDecimal detailTotal = importDetail.getPrice().multiply(BigDecimal.valueOf(importDetail.getQuantity()));
            BigDecimal newInvoiceTotal = importInvoice.getTotalAmount().subtract(detailTotal);
            importInvoice.setTotalAmount(newInvoiceTotal);
            import_InvoiceRepo.save(importInvoice);
            log.info("Reduced invoice total to {}", newInvoiceTotal);

            Inventory inventory = new Inventory();
            inventory.setProductId(importDetail.getProductId());
            inventory.setChangeQuantity(-importDetail.getQuantity());
            inventory.setDate(importInvoice.getDate().atStartOfDay());
            inventory.setReason("Cancel Import");
            inventoryRepo.save(inventory);

            import_DetailRepo.delete(importDetail);
            log.info("Import detail deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting import detail with ID: {}", id, e);
            throw e;
        }
    }
}
