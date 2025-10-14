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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class Sale_DetailService {
    private final Sale_InvoiceRepo sale_InvoiceRepo;
    private final InventoryRepo inventoryRepo;
    private Sale_DetailRepo saleDetailRepo;
    private ObjectMapper objectMapper;
    private ProductRepo productRepo;
    public List<Sale_DetailResponse> getSaleDetailBySaleId(Long saleId){
        List<Sale_DetailIF> results = saleDetailRepo.findAllBySaleId(saleId);
        List<Sale_DetailResponse> response = objectMapper
                .convertValue(results, new TypeReference<List<Sale_DetailResponse>>() {});
        return response;
    }
    public Sale_DetailResponse getSaleDetailById(Long id){
        Sale_DetailIF result = saleDetailRepo.findSaleDetailById(id);
        Sale_DetailResponse response = objectMapper.convertValue(result, Sale_DetailResponse.class);
        return response;
    }
    public Sale_DetailResponse createSaleDetail(Sale_Detail saleDetail){
        Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                .orElseThrow(() -> new RuntimeException("Sale Invoice Not Found"));
        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        ProductIF result = productRepo.findProductByIdWithRelations(saleDetail.getProductId());
        ProductResponse productResponse = objectMapper.convertValue(result, new TypeReference<ProductResponse>(){});
        saleDetail.setSalePrice(BigDecimal.valueOf(productResponse.getFinalPrice()));
        saleDetailRepo.save(saleDetail);

        int qty = saleDetail.getQuantity();
        if(qty > productResponse.getStockQuantity()){
            throw new RuntimeException("Stock Quantity Exceeded");
        }
        product.setStockQuantity(product.getStockQuantity() - qty);
        productRepo.save(product);

        BigDecimal oldTotalAmount = saleInvoice.getTotalAmount();
        BigDecimal detailTotal = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));
        BigDecimal newTotal = oldTotalAmount.add(detailTotal);
        saleInvoice.setTotalAmount(newTotal);
        sale_InvoiceRepo.save(saleInvoice);

        Inventory inventory = new Inventory();
        inventory.setProductId(saleDetail.getProductId());
        inventory.setChangeQuantity(-saleDetail.getQuantity());
        inventory.setDate(saleInvoice.getDate().atStartOfDay());
        inventory.setReason("Sale Product");
        inventoryRepo.save(inventory);

        Sale_DetailIF result2 = saleDetailRepo.findSaleDetailById(saleDetail.getSaleDetailId());
        Sale_DetailResponse response2 = objectMapper.convertValue(result2, Sale_DetailResponse.class);
        return response2;
    }

    public Sale_DetailResponse updateSaleDetail(Long id, Sale_Detail saleDetail){
        Sale_Detail saleDetail1 = saleDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale Detail Not Found"));
        Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                .orElseThrow(() -> new RuntimeException("Sale Invoice Not Found"));
        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        ProductIF result = productRepo.findProductByIdWithRelations(saleDetail.getProductId());
        ProductResponse response = objectMapper.convertValue(result, new TypeReference<ProductResponse>(){});
        saleDetail.setSalePrice(BigDecimal.valueOf(response.getFinalPrice()));

        saleDetail.setSaleDetailId(saleDetail1.getSaleDetailId());
        saleDetailRepo.save(saleDetail);

        int  qty = saleDetail.getQuantity();
        if(qty - saleDetail1.getQuantity() -product.getStockQuantity() > 0){
            throw new RuntimeException("Stock Quantity Exceeded");
        }
        product.setStockQuantity(product.getStockQuantity() + saleDetail1.getQuantity() - qty);
        productRepo.save(product);

        BigDecimal oldDetailTotal = saleDetail1.getSalePrice().multiply(BigDecimal.valueOf(saleDetail1.getQuantity()));
        BigDecimal newDetailTotal = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));

        BigDecimal newInvoiceTotal = saleInvoice.getTotalAmount().subtract(oldDetailTotal).add(newDetailTotal);
        saleInvoice.setTotalAmount(newInvoiceTotal);
        sale_InvoiceRepo.save(saleInvoice);

        Inventory inventory = new Inventory();
        inventory.setProductId(saleDetail.getProductId());
        inventory.setChangeQuantity(saleDetail.getQuantity() - saleDetail1.getQuantity());
        inventory.setReason("Updated Sale Detail");
        inventory.setDate(LocalDateTime.now());
        inventoryRepo.save(inventory);

        Sale_DetailIF result2 = saleDetailRepo.findSaleDetailById(saleDetail.getSaleDetailId());
        Sale_DetailResponse response2 = objectMapper.convertValue(result2, Sale_DetailResponse.class);
        return response2;
    }

    public void deleteSaleDetailById(Long id){
        Sale_Detail saleDetail = saleDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale Detail Not Found"));
        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(() ->  new RuntimeException("Product Not Found"));
        Sale_Invoice saleInvoice = sale_InvoiceRepo.findById(saleDetail.getSaleId())
                .orElseThrow(() ->  new RuntimeException("Sale Invoice Not Found"));

        int qty = saleDetail.getQuantity();
        product.setStockQuantity(product.getStockQuantity() + qty);
        productRepo.save(product);

        BigDecimal oldTotalAmount = saleDetail.getSalePrice().multiply(BigDecimal.valueOf(qty));
        BigDecimal newTotalAmount = saleInvoice.getTotalAmount().subtract(oldTotalAmount);
        saleInvoice.setTotalAmount(newTotalAmount);
        sale_InvoiceRepo.save(saleInvoice);

        Inventory inventory = new Inventory();
        inventory.setProductId(saleDetail.getProductId());
        inventory.setChangeQuantity(saleDetail.getQuantity());
        inventory.setReason("Deleted Sale Detail");
        inventory.setDate(LocalDateTime.now());
        inventoryRepo.save(inventory);

        saleDetailRepo.delete(saleDetail);
    }
}
