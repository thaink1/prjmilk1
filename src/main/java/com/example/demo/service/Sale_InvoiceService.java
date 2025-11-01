package com.example.demo.service;

import com.example.demo.dto.Sale_InvoiceIF;
import com.example.demo.dto.Sale_InvoiceResponse;
import com.example.demo.model.Inventory;
import com.example.demo.model.Product;
import com.example.demo.model.Sale_Detail;
import com.example.demo.model.Sale_Invoice;
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
public class Sale_InvoiceService {

    private final Sale_DetailRepo sale_DetailRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final Sale_InvoiceRepo sale_invoiceRepo;
    private final ObjectMapper objectMapper;
    private final CustomerRepo customerRepo;

    public List<Sale_InvoiceResponse> getAllSaleInvoices() {
        try {
            log.info("Fetching all sale invoices...");
            List<Sale_InvoiceIF> results = sale_invoiceRepo.findAllSale_InvoiceWithRelations();
            List<Sale_InvoiceResponse> responses = objectMapper.convertValue(results, new TypeReference<>() {});
            log.info("Fetched {} sale invoices successfully", responses.size());
            return responses;
        } catch (Exception e) {
            log.error("Error fetching sale invoices", e);
            throw e;
        }
    }

    public Sale_InvoiceResponse getSaleInvoiceById(Long id) {
        try {
            log.info("Fetching sale invoice with ID: {}", id);
            Sale_Invoice saleInvoice = sale_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Sale invoice not found with ID: {}", id);
                        return new RuntimeException("SaleInvoice with id " + id + " not found!");
                    });

            Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(id);
            Sale_InvoiceResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            log.info("Fetched sale invoice successfully with ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error fetching sale invoice with ID: {}", id, e);
            throw e;
        }
    }

    public Sale_InvoiceResponse createSaleInvoice(Sale_Invoice saleInvoice) {
        try {
            log.info("Creating new sale invoice for customer ID: {}", saleInvoice.getCustomerId());

            if (!customerRepo.existsById(saleInvoice.getCustomerId())) {
                log.warn("Customer not found with ID: {}", saleInvoice.getCustomerId());
                throw new RuntimeException("Customer with id " + saleInvoice.getCustomerId() + " not found!");
            }

            sale_invoiceRepo.save(saleInvoice);
            log.info("Sale invoice created successfully with ID: {}", saleInvoice.getSaleId());

            Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(saleInvoice.getSaleId());
            Sale_InvoiceResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            return response;
        } catch (Exception e) {
            log.error("Error creating sale invoice for customer ID: {}", saleInvoice.getCustomerId(), e);
            throw e;
        }
    }

    public Sale_InvoiceResponse updateSaleInvoice(long id, Sale_Invoice saleInvoice) {
        try {
            log.info("Updating sale invoice with ID: {}", id);

            Sale_Invoice existing = sale_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Sale invoice not found with ID: {}", id);
                        return new RuntimeException("SaleInvoice with id " + id + " not found!");
                    });

            if (!customerRepo.existsById(saleInvoice.getCustomerId())) {
                log.warn("Customer not found with ID: {}", saleInvoice.getCustomerId());
                throw new RuntimeException("Customer with id " + saleInvoice.getCustomerId() + " not found!");
            }

            existing.setCustomerId(saleInvoice.getCustomerId());
            existing.setTotalAmount(saleInvoice.getTotalAmount());
            sale_invoiceRepo.save(existing);

            Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(id);
            Sale_InvoiceResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            log.info("Updated sale invoice successfully with ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error updating sale invoice with ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public void deleteSaleInvoice(long id) {
        try {
            log.info("Deleting sale invoice with ID: {}", id);

            Sale_Invoice saleInvoice = sale_invoiceRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Sale invoice not found with ID: {}", id);
                        return new RuntimeException("SaleInvoice with id " + id + " not found!");
                    });

            List<Sale_Detail> saleDetails = sale_DetailRepo.findBySaleId(saleInvoice.getSaleId());
            log.info("Found {} sale details for invoice ID: {}", saleDetails.size(), id);

            for (Sale_Detail saleDetail : saleDetails) {
                Product product = productRepo.findById(saleDetail.getProductId())
                        .orElseThrow(() -> {
                            log.warn("Product not found with ID: {}", saleDetail.getProductId());
                            return new RuntimeException("Product not found!");
                        });

                int newStock = product.getStockQuantity() + saleDetail.getQuantity();
                product.setStockQuantity(newStock);
                productRepo.save(product);

                Inventory inventory = new Inventory();
                inventory.setProductId(product.getProductId());
                inventory.setChangeQuantity(saleDetail.getQuantity());
                inventory.setDate(LocalDateTime.now());
                inventory.setReason("Delete sale invoice");
                inventoryRepo.save(inventory);

                log.info("Restored stock and inventory for product ID: {}", product.getProductId());
            }

            sale_DetailRepo.deleteAllBySaleId(id);
            sale_invoiceRepo.deleteById(id);

            log.info("Sale invoice deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting sale invoice with ID: {}", id, e);
            throw e;
        }
    }
}
