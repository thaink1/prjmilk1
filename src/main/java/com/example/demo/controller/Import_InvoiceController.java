package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Import_InvoiceResponse;
import com.example.demo.model.Import_Invoice;
import com.example.demo.service.Import_InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/import_invoice")
@Slf4j
public class Import_InvoiceController {

    private final Import_InvoiceService importInvoiceService;

    @GetMapping("")
    public BaseResponse<List<Import_InvoiceResponse>> getAll() {
        BaseResponse<List<Import_InvoiceResponse>> response = new BaseResponse<>();
        try {
            log.info("Request to fetch all import invoices");
            response.setBody(importInvoiceService.getAll());
            response.setMessage("Fetched all import invoices successfully");
        } catch (Exception e) {
            log.error("Error fetching import invoices", e);
            response.setMessage("Failed to fetch import invoices: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Import_InvoiceResponse> getImportInvoiceById(@PathVariable Long id) {
        BaseResponse<Import_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Fetching import invoice by ID: {}", id);
            response.setBody(importInvoiceService.findById(id));
            response.setMessage("Fetched import invoice successfully");
        } catch (Exception e) {
            log.error("Error fetching import invoice with ID: {}", id, e);
            response.setMessage("Failed to fetch import invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Import_InvoiceResponse> create(@Valid @RequestBody Import_Invoice importInvoice) {
        BaseResponse<Import_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Creating new import invoice for distributorId: {}", importInvoice.getDistributorId());
            response.setBody(importInvoiceService.createImport_Invoice(importInvoice));
            response.setMessage("Import invoice created successfully");
        } catch (Exception e) {
            log.error("Error creating import invoice", e);
            response.setMessage("Failed to create import invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Import_InvoiceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody Import_Invoice importInvoice) {
        BaseResponse<Import_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Updating import invoice ID: {}", id);
            response.setBody(importInvoiceService.updateImport_Invoice(id, importInvoice));
            response.setMessage("Import invoice updated successfully");
        } catch (Exception e) {
            log.error("Error updating import invoice ID: {}", id, e);
            response.setMessage("Failed to update import invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteById(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Deleting import invoice with ID: {}", id);
            importInvoiceService.deleteImport_Invoice(id);
            response.setMessage("Import invoice deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting import invoice ID: {}", id, e);
            response.setMessage("Failed to delete import invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
