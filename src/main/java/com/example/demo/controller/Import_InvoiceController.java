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
            List<Import_InvoiceResponse> invoices = importInvoiceService.getAll();
            response.setBody(invoices);
            response.setMessage("Fetched all import invoices successfully");
        } catch (Exception e) {
            log.error("Error fetching import invoices", e);
            throw e;
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
            Import_InvoiceResponse invoice = importInvoiceService.findById(id);
            response.setBody(invoice);
            response.setMessage("Fetched import invoice successfully");
        } catch (Exception e) {
            log.error("Error fetching import invoice with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Import_InvoiceResponse> create(@Valid @RequestBody Import_Invoice importInvoice) {
        log.info("Creating new import invoice for distributorId: {}", importInvoice.getDistributorId());
        Import_InvoiceResponse created = importInvoiceService.createImport_Invoice(importInvoice);

        BaseResponse<Import_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(created);
        response.setMessage("Import invoice created successfully");
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
            Import_InvoiceResponse updated = importInvoiceService.updateImport_Invoice(id, importInvoice);
            response.setBody(updated);
            response.setMessage("Import invoice updated successfully");
        } catch (Exception e) {
            log.error("Error updating import invoice ID: {}", id, e);
            throw e;
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
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
