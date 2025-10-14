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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class Sale_InvoiceService {
    private final Sale_DetailRepo sale_DetailRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private Sale_InvoiceRepo sale_invoiceRepo;
    private ObjectMapper objectMapper;
    private CustomerRepo customerRepo;
    public List<Sale_InvoiceResponse> getAllSaleInvoices(){
        List<Sale_InvoiceIF> results = sale_invoiceRepo.findAllSale_InvoiceWithRelations();
        List<Sale_InvoiceResponse> sale_invoiceResponseList = objectMapper
                .convertValue(results, new TypeReference<List<Sale_InvoiceResponse>>() {
        });
        return sale_invoiceResponseList;
    }

    public Sale_InvoiceResponse getSaleInvoiceById(Long id){
        Sale_Invoice saleInvoice = sale_invoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SaleInvoice with id " + id + " not found!"));
        Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(id);
        Sale_InvoiceResponse response = objectMapper
                .convertValue(result, new TypeReference<Sale_InvoiceResponse>() {});
        return response;
    }

    public Sale_InvoiceResponse createSaleInvoice(Sale_Invoice saleInvoice){
        if(!customerRepo.existsById(saleInvoice.getCustomerId())){
            throw new RuntimeException("Customer with id " + saleInvoice.getCustomerId() + " not found!");
        }
        sale_invoiceRepo.save(saleInvoice);
        Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(saleInvoice.getSaleId());
        Sale_InvoiceResponse response = objectMapper.convertValue(result, new TypeReference<Sale_InvoiceResponse>() {});
        return response;
    }

    public Sale_InvoiceResponse updateSaleInvoice(long id, Sale_Invoice saleInvoice){
        Sale_Invoice saleInvoice1 = sale_invoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SaleInvoice with id " + id + " not found!"));
        if(!customerRepo.existsById(saleInvoice.getCustomerId())){
            throw new RuntimeException("Customer with id " + saleInvoice.getCustomerId() + " not found!");
        }
        saleInvoice1.setCustomerId(saleInvoice.getCustomerId());
        saleInvoice1.setTotalAmount(saleInvoice.getTotalAmount());
        sale_invoiceRepo.save(saleInvoice1);

        Sale_InvoiceIF result = sale_invoiceRepo.finSale_InvoiceBySaleIdWithRelations(id);
        Sale_InvoiceResponse response = objectMapper.convertValue(result, new TypeReference<Sale_InvoiceResponse>() {});
        return response;
    }

    @Transactional
    public void deleteSaleInvoice(long id){
        Sale_Invoice saleInvoice = sale_invoiceRepo.findById(id)
                .orElseThrow(() ->  new RuntimeException("SaleInvoice with id " + id + " not found!"));
        List<Sale_Detail> saleDetails = sale_DetailRepo.findBySaleId(saleInvoice.getSaleId());

        for (Sale_Detail saleDetail : saleDetails) {
            Product product = productRepo.findById(saleDetail.getProductId())
                    .orElseThrow(() ->  new RuntimeException("Product not found!"));

            int newStock = product.getStockQuantity() + saleDetail.getQuantity();
            product.setStockQuantity(newStock);
            productRepo.save(product);

            Inventory inventory = new Inventory();
            inventory.setProductId(product.getProductId());
            inventory.setChangeQuantity(saleDetail.getQuantity());
            inventory.setDate(LocalDateTime.now());
            inventory.setReason("Delete sale invoice");
            inventoryRepo.save(inventory);
        }
        sale_DetailRepo.deleteAllBySaleId(id);
        sale_invoiceRepo.deleteById(id);
    }

}
