package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Sale_InvoiceResponse;
import com.example.demo.model.Sale_Invoice;
import com.example.demo.service.Sale_InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sale_invoice")
@AllArgsConstructor
@Slf4j
public class Sale_InvoiceController {

    private final Sale_InvoiceService sale_invoiceService;

    @GetMapping("")
    public BaseResponse<List<Sale_InvoiceResponse>> getAllSaleInvoices() {
        BaseResponse<List<Sale_InvoiceResponse>> response = new BaseResponse<>();
        try {
            log.info("Request to fetch all sale invoices");
            response.setBody(sale_invoiceService.getAllSaleInvoices());
            response.setMessage("Fetched all sale invoices successfully");
        } catch (Exception e) {
            log.error("Error fetching all sale invoices", e);
            response.setMessage("Failed to fetch sale invoices: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> getSaleInvoice(@PathVariable Long id) {
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Request to fetch sale invoice with ID: {}", id);
            response.setBody(sale_invoiceService.getSaleInvoiceById(id));
            response.setMessage("Fetched sale invoice successfully");
        } catch (Exception e) {
            log.error("Error fetching sale invoice with ID: {}", id, e);
            response.setMessage("Failed to fetch sale invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Sale_InvoiceResponse> createSaleInvoice(@Valid @RequestBody Sale_Invoice sale_invoice) {
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Request to create sale invoice for customerId: {}", sale_invoice.getCustomerId());
            response.setBody(sale_invoiceService.createSaleInvoice(sale_invoice));
            response.setMessage("Sale invoice created successfully");
        } catch (Exception e) {
            log.error("Error creating sale invoice for customerId: {}", sale_invoice.getCustomerId(), e);
            response.setMessage("Failed to create sale invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> updateSaleInvoice(
            @PathVariable Long id,
            @Valid @RequestBody Sale_Invoice sale_invoice) {
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        try {
            log.info("Request to update sale invoice with ID: {}", id);
            response.setBody(sale_invoiceService.updateSaleInvoice(id, sale_invoice));
            response.setMessage("Sale invoice updated successfully");
        } catch (Exception e) {
            log.error("Error updating sale invoice with ID: {}", id, e);
            response.setMessage("Failed to update sale invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleInvoice(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete sale invoice with ID: {}", id);
            sale_invoiceService.deleteSaleInvoice(id);
            response.setMessage("Sale invoice deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting sale invoice with ID: {}", id, e);
            response.setMessage("Failed to delete sale invoice: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
