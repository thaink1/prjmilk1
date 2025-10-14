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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class Import_InvoiceService {
    private Import_InvoiceRepo import_invoiceRepo;
    private ObjectMapper objectMapper;
    private DistributorRepo distributorRepo;
    private Import_DetailRepo  import_DetailRepo;
    private ProductRepo productRepo;
    private InventoryRepo inventoryRepo;
public List<Import_InvoiceResponse> getAll(){
    List<Import_InvoiceIF> results = import_invoiceRepo.findAllImport_InvoiceWithRelations();
    List<Import_InvoiceResponse> response = objectMapper.
            convertValue(results, new TypeReference<List<Import_InvoiceResponse>>() {});
    return response;
}

public Import_InvoiceResponse findById(long id){
    Import_Invoice importInvoice = import_invoiceRepo.
            findById(id).orElseThrow(() -> new RuntimeException("Import invoice not found"));
    Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(importInvoice.getImportId());
    Import_InvoiceResponse response = objectMapper.
            convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
    return response;
}

public Import_InvoiceResponse createImport_Invoice(Import_Invoice request){
    if (!distributorRepo.existsById(request.getDistributorId())) {
        throw new RuntimeException("Distributor not found");
    }
    import_invoiceRepo.save(request);
    Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(request.getImportId());
    Import_InvoiceResponse response = objectMapper.
            convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
    return response;
}

public Import_InvoiceResponse updateImport_Invoice(long id, Import_Invoice request){
    Import_Invoice importInvoice = import_invoiceRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Import invoice not found"));
    if (!distributorRepo.existsById(request.getDistributorId())) {
        throw new RuntimeException("Distributor not found");
    }
    importInvoice.setDate(request.getDate());
    importInvoice.setDistributorId(request.getDistributorId());
    importInvoice.setTotalAmount(request.getTotalAmount());
    import_invoiceRepo.save(importInvoice);
    Import_InvoiceIF results = import_invoiceRepo.findImport_InvoiceByIdWithRelations(id);
    Import_InvoiceResponse response = objectMapper.
            convertValue(results, new TypeReference<Import_InvoiceResponse>() {});
    return response;
}

@Transactional
public void deleteImport_Invoice(long id){
    Import_Invoice importInvoice = import_invoiceRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Import invoice not found"));

    List<Import_Detail> importDetails = import_DetailRepo.findByImportId(importInvoice.getImportId());

    for (Import_Detail detail : importDetails) {
        Product product = productRepo.findById(detail.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newStock = product.getStockQuantity() - detail.getQuantity();
        product.setStockQuantity(newStock);
        productRepo.save(product);

        Inventory inventory = new Inventory();
        inventory.setProductId(product.getProductId());
        inventory.setChangeQuantity(-detail.getQuantity());
        inventory.setDate(LocalDateTime.now());
        inventory.setReason("Delete Import");
        inventoryRepo.save(inventory);
    }
    import_DetailRepo.deleteAllByImportId(importInvoice.getImportId());
    import_invoiceRepo.deleteById(importInvoice.getImportId());
}
}
