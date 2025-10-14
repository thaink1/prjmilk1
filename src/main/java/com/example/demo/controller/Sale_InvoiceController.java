package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Sale_InvoiceResponse;
import com.example.demo.model.Sale_Invoice;
import com.example.demo.service.Sale_InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sale_invoice")
@AllArgsConstructor
public class Sale_InvoiceController {
    private Sale_InvoiceService sale_invoiceService;
    @GetMapping("")
    public BaseResponse<List<Sale_InvoiceResponse>> getAllSaleInvoices(){
        BaseResponse<List<Sale_InvoiceResponse>> response = new BaseResponse<>();
        response.setBody(sale_invoiceService.getAllSaleInvoices());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> getSaleInvoice(@PathVariable Long id){
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(sale_invoiceService.getSaleInvoiceById(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Sale_InvoiceResponse> createSaleInvoice(@Valid @RequestBody Sale_Invoice sale_invoice){
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(sale_invoiceService.createSaleInvoice(sale_invoice));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_InvoiceResponse> updateSaleInvoice(@Valid @RequestBody Sale_Invoice sale_invoice, @PathVariable Long id){
        BaseResponse<Sale_InvoiceResponse> response = new BaseResponse<>();
        response.setBody(sale_invoiceService.updateSaleInvoice(id, sale_invoice));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleInvoice(@PathVariable Long id){
        sale_invoiceService.deleteSaleInvoice(id);
        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Delete sale_invoice by ID successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
