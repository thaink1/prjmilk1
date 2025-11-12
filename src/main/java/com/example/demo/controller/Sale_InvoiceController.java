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

    private final Sale_InvoiceService saleInvoiceService;

    @GetMapping("")
    public BaseResponse<List<Sale_InvoiceResponse>> getAllSaleInvoices() {
        log.info("Request to fetch all sale invoices");
        List<Sale_InvoiceResponse> invoices = saleInvoiceService.getAllSaleInvoices();

        BaseResponse<List<Sale_InvoiceResponse>> response = new BaseResponse<>();
        response.setBody(invoices);
        response.setMessage("Fetched all sale invoices successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> getSaleInvoice(@PathVariable Long id) {
        log.info("Fetching sale invoice with ID: {}", id);
        Sale_InvoiceResponse invoice = saleInvoiceService.getSaleInvoiceById(id);

        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(invoice);
        response.setMessage("Fetched sale invoice successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Sale_InvoiceResponse> createSaleInvoice(@Valid @RequestBody Sale_Invoice saleInvoice) {
        log.info("Creating sale invoice for customerId: {}", saleInvoice.getCustomerId());
        Sale_InvoiceResponse created = saleInvoiceService.createSaleInvoice(saleInvoice);

        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(created);
        response.setMessage("Sale invoice created successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> updateSaleInvoice(
            @PathVariable Long id,
            @Valid @RequestBody Sale_Invoice saleInvoice) {
        log.info("Updating sale invoice with ID: {}", id);
        Sale_InvoiceResponse updated = saleInvoiceService.updateSaleInvoice(id, saleInvoice);

        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(updated);
        response.setMessage("Sale invoice updated successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleInvoice(@PathVariable Long id) {
        log.info("Deleting sale invoice with ID: {}", id);
        saleInvoiceService.deleteSaleInvoice(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Sale invoice deleted successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
