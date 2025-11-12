package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Inventory;
import com.example.demo.model.Product;
import com.example.demo.model.Sale_Detail;
import com.example.demo.model.Sale_Invoice;
import com.example.demo.repo.InventoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.Sale_DetailRepo;
import com.example.demo.repo.Sale_InvoiceRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class Sale_DetailService {

    private final Sale_InvoiceRepo sale_InvoiceRepo;
    private final InventoryRepo inventoryRepo;
    private final Sale_DetailRepo saleDetailRepo;
    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;
    private final InventoryHistoryService inventoryHistoryService;


    public List<Sale_DetailResponse> getSaleDetailBySaleId(Long saleId) {
        try {
            log.info("Fetching sale details for sale ID: {}", saleId);
            List<Sale_DetailIF> results = saleDetailRepo.findAllBySaleId(saleId);
            List<Sale_DetailResponse> response = objectMapper.convertValue(results, new TypeReference<>() {});
            log.info("Fetched {} sale details for sale ID: {}", response.size(), saleId);
            return response;
        } catch (Exception e) {
            log.error("Error fetching sale details for sale ID: {}", saleId, e);
            throw e;
        }
    }

    public Sale_DetailResponse getSaleDetailById(Long id) {
        try {
            log.info("Fetching sale detail with ID: {}", id);
            Sale_DetailIF result = saleDetailRepo.findSaleDetailById(id);
            if (result == null) {
                log.warn("Sale detail not found with ID: {}", id);
                throw new RuntimeException("Sale Detail Not Found");
            }
            Sale_DetailResponse response = objectMapper.convertValue(result, Sale_DetailResponse.class);
            log.info("Fetched sale detail successfully with ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error fetching sale detail with ID: {}", id, e);
            throw e;
        }
    }

    public Sale_DetailResponse createSaleDetail(Sale_Detail saleDetail) {
        try {
            log.info("Creating sale detail for sale ID: {}, product ID: {}", saleDetail.getSaleId(), saleDetail.getProductId());

            Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                    .orElseThrow(() -> {
                        log.warn("Sale invoice not found with ID: {}", saleDetail.getSaleId());
                        return new RuntimeException("Sale Invoice Not Found");
                    });

            Product product = productRepo.findById(saleDetail.getProductId())
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", saleDetail.getProductId());
                        return new RuntimeException("Product Not Found");
                    });

            ProductIF result = productRepo.findProductByIdWithRelations(saleDetail.getProductId());
            ProductResponse productResponse = objectMapper.convertValue(result, new TypeReference<>() {});
            saleDetail.setSalePrice(BigDecimal.valueOf(productResponse.getFinalPrice()));

            int qty = saleDetail.getQuantity();
            if (qty > productResponse.getStockQuantity()) {
                log.warn("Stock quantity exceeded for product ID: {}", saleDetail.getProductId());
                throw new RuntimeException("Stock Quantity Exceeded");
            }

            saleDetailRepo.save(saleDetail);
            log.info("Saved sale detail with ID: {}", saleDetail.getSaleDetailId());

            product.setStockQuantity(product.getStockQuantity() - qty);
            productRepo.save(product);

            BigDecimal detailTotal = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));
            saleInvoice.setTotalAmount(saleInvoice.getTotalAmount().add(detailTotal));
            sale_InvoiceRepo.save(saleInvoice);

            Inventory inventory = new Inventory();
            inventory.setProductId(saleDetail.getProductId());
            inventory.setChangeQuantity(-qty);
            inventory.setDate(saleInvoice.getDate().atStartOfDay());
            inventory.setReason("Sale Product");
            inventoryRepo.save(inventory);
            inventoryHistoryService.createHistory(inventory);


            Sale_DetailIF result2 = saleDetailRepo.findSaleDetailById(saleDetail.getSaleDetailId());
            Sale_DetailResponse response2 = objectMapper.convertValue(result2, Sale_DetailResponse.class);

            log.info("Created sale detail successfully for sale ID: {}", saleDetail.getSaleId());
            return response2;
        } catch (Exception e) {
            log.error("Error creating sale detail for sale ID: {}", saleDetail.getSaleId(), e);
            throw e;
        }
    }

    public Sale_DetailResponse updateSaleDetail(Long id, Sale_Detail saleDetail) {
        try {
            log.info("Updating sale detail with ID: {}", id);

            Sale_Detail oldDetail = saleDetailRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Sale detail not found with ID: {}", id);
                        return new RuntimeException("Sale Detail Not Found");
                    });

            Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                    .orElseThrow(() -> new RuntimeException("Sale Invoice Not Found"));

            Product product = productRepo.findById(saleDetail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            ProductIF result = productRepo.findProductByIdWithRelations(saleDetail.getProductId());
            ProductResponse productResponse = objectMapper.convertValue(result, new TypeReference<>() {});
            saleDetail.setSalePrice(BigDecimal.valueOf(productResponse.getFinalPrice()));

            int qty = saleDetail.getQuantity();
            if (qty - oldDetail.getQuantity() - product.getStockQuantity() > 0) {
                log.warn("Stock quantity exceeded for product ID: {}", saleDetail.getProductId());
                throw new RuntimeException("Stock Quantity Exceeded");
            }

            product.setStockQuantity(product.getStockQuantity() + oldDetail.getQuantity() - qty);
            productRepo.save(product);

            BigDecimal oldDetailTotal = oldDetail.getSalePrice().multiply(BigDecimal.valueOf(oldDetail.getQuantity()));
            BigDecimal newDetailTotal = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));
            saleInvoice.setTotalAmount(saleInvoice.getTotalAmount().subtract(oldDetailTotal).add(newDetailTotal));
            sale_InvoiceRepo.save(saleInvoice);

            saleDetail.setSaleDetailId(id);
            saleDetailRepo.save(saleDetail);

            Inventory inventory = new Inventory();
            inventory.setProductId(saleDetail.getProductId());
            inventory.setChangeQuantity(qty - oldDetail.getQuantity());
            inventory.setReason("Updated Sale Detail");
            inventory.setDate(LocalDateTime.now());
            inventoryRepo.save(inventory);
            inventoryHistoryService.createHistory(inventory);


            Sale_DetailIF result2 = saleDetailRepo.findSaleDetailById(id);
            Sale_DetailResponse response2 = objectMapper.convertValue(result2, Sale_DetailResponse.class);

            log.info("Updated sale detail successfully with ID: {}", id);
            return response2;
        } catch (Exception e) {
            log.error("Error updating sale detail with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteSaleDetailById(Long id) {
        try {
            log.info("Deleting sale detail with ID: {}", id);

            Sale_Detail saleDetail = saleDetailRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Sale detail not found with ID: {}", id);
                        return new RuntimeException("Sale Detail Not Found");
                    });

            Product product = productRepo.findById(saleDetail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
            Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                    .orElseThrow(() -> new RuntimeException("Sale Invoice Not Found"));

            int qty = saleDetail.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + qty);
            productRepo.save(product);

            BigDecimal detailTotal = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));
            saleInvoice.setTotalAmount(saleInvoice.getTotalAmount().subtract(detailTotal));
            sale_InvoiceRepo.save(saleInvoice);

            Inventory inventory = new Inventory();
            inventory.setProductId(saleDetail.getProductId());
            inventory.setChangeQuantity(qty);
            inventory.setReason("Deleted Sale Detail");
            inventory.setDate(LocalDateTime.now());
            inventoryRepo.save(inventory);
            inventoryHistoryService.createHistory(inventory);


            saleDetailRepo.delete(saleDetail);
            log.info("Deleted sale detail successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting sale detail with ID: {}", id, e);
            throw e;
        }
    }
}
