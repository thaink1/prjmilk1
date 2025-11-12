package com.example.demo.service;

import com.example.demo.dto.Import_InvoiceIF;
import com.example.demo.dto.Import_InvoiceResponse;
import com.example.demo.model.Import_Detail;
import com.example.demo.model.Import_Invoice;
import com.example.demo.model.Inventory;
import com.example.demo.model.Product;
import com.example.demo.repo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class Import_InvoiceService {

    private final Import_InvoiceRepo import_invoiceRepo;
    private final ObjectMapper objectMapper;
    private final DistributorRepo distributorRepo;
    private final Import_DetailRepo import_DetailRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final InventoryHistoryService inventoryHistoryService;

    public List<Import_InvoiceResponse> getAll() {
        try {
            log.info("Fetching all import invoices...");
            List<Import_InvoiceIF> results = import_invoiceRepo.findAllImport_InvoiceWithRelations();
            List<Import_InvoiceResponse> response =
                    objectMapper.convertValue(results, new TypeReference<List<Import_InvoiceResponse>>() {});
            log.info("Fetched {} import invoices successfully", response.size());
            return response;
        } catch (Exception e) {
            log.error("Error while fetching import invoices", e);
            throw e;
        }
    }

    public Import_InvoiceResponse findById(long id) {
        try {
            log.info("Fetching import invoice with ID: {}", id);
            Import_Invoice importInvoice = import_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import invoice not found with ID: {}", id);
                        return new RuntimeException("Import invoice not found");
                    });

            Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(importInvoice.getImportId());
            Import_InvoiceResponse response =
                    objectMapper.convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
            log.info("Fetched import invoice successfully: {}", importInvoice.getImportId());
            return response;
        } catch (Exception e) {
            log.error("Error fetching import invoice with ID: {}", id, e);
            throw e;
        }
    }

    public Import_InvoiceResponse createImport_Invoice(Import_Invoice request) {
        try {
            log.info("Creating new import invoice for distributor ID: {}", request.getDistributorId());
            if (!distributorRepo.existsById(request.getDistributorId())) {
                log.warn("Distributor not found with ID: {}", request.getDistributorId());
                throw new RuntimeException("Distributor not found");
            }

            import_invoiceRepo.save(request);
            log.info("Import invoice created successfully with ID: {}", request.getImportId());

            Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(request.getImportId());
            Import_InvoiceResponse response =
                    objectMapper.convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
            return response;
        } catch (Exception e) {
            log.error("Error while creating import invoice for distributor ID: {}", request.getDistributorId(), e);
            throw e;
        }
    }

    public Import_InvoiceResponse updateImport_Invoice(long id, Import_Invoice request) {
        try {
            log.info("Updating import invoice with ID: {}", id);
            Import_Invoice importInvoice = import_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import invoice not found with ID: {}", id);
                        return new RuntimeException("Import invoice not found");
                    });

            if (!distributorRepo.existsById(request.getDistributorId())) {
                log.warn("Distributor not found with ID: {}", request.getDistributorId());
                throw new RuntimeException("Distributor not found");
            }

            importInvoice.setDate(request.getDate());
            importInvoice.setDistributorId(request.getDistributorId());
            importInvoice.setTotalAmount(request.getTotalAmount());
            import_invoiceRepo.save(importInvoice);
            log.info("Updated import invoice successfully with ID: {}", id);

            Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(id);
            Import_InvoiceResponse response =
                    objectMapper.convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
            return response;
        } catch (Exception e) {
            log.error("Error while updating import invoice with ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public void deleteImport_Invoice(long id) {
        try {
            log.info("Deleting import invoice with ID: {}", id);
            Import_Invoice importInvoice = import_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Import invoice not found with ID: {}", id);
                        return new RuntimeException("Import invoice not found");
                    });

            List<Import_Detail> importDetails = import_DetailRepo.findByImportId(importInvoice.getImportId());
            log.info("Found {} import details linked to invoice {}", importDetails.size(), id);

            for (Import_Detail detail : importDetails) {
                Product product = productRepo.findById(detail.getProductId())
                        .orElseThrow(() -> {
                            log.warn("Product not found with ID: {}", detail.getProductId());
                            return new RuntimeException("Product not found");
                        });

                int newStock = product.getStockQuantity() - detail.getQuantity();
                product.setStockQuantity(newStock);
                productRepo.save(product);
                log.info("Updated stock for product {} to {}", product.getProductId(), newStock);

                Inventory inventory = new Inventory();
                inventory.setProductId(product.getProductId());
                inventory.setChangeQuantity(-detail.getQuantity());
                inventory.setDate(LocalDateTime.now());
                inventory.setReason("Delete Import");
                inventoryRepo.save(inventory);
                inventoryHistoryService.createHistory(inventory);

                log.info("Recorded inventory change for product {} after import delete", product.getProductId());
            }

            import_DetailRepo.deleteAllByImportId(importInvoice.getImportId());
            import_invoiceRepo.deleteById(importInvoice.getImportId());
            log.info("Import invoice and its details deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting import invoice with ID: {}", id, e);
            throw e;
        }
    }
}
