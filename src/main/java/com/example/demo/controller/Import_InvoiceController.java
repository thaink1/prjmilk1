package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Import_InvoiceResponse;
import com.example.demo.model.Import_Invoice;
import com.example.demo.service.Import_InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/import_invoice")
public class Import_InvoiceController {

    private final Import_InvoiceService importInvoiceService;

    @GetMapping("")
    public BaseResponse<List<Import_InvoiceResponse>> getAll() {
        BaseResponse<List<Import_InvoiceResponse>> baseResponse = new BaseResponse<>();
        baseResponse.setBody(importInvoiceService.getAll());
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @GetMapping("/{id}")
    public BaseResponse<Import_InvoiceResponse> getImport_InvoiceById(@PathVariable Long id) {
        BaseResponse<Import_InvoiceResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(importInvoiceService.findById(id));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PostMapping("")
    public BaseResponse<Import_InvoiceResponse> create(@Valid @RequestBody Import_Invoice importInvoice) {
        BaseResponse<Import_InvoiceResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(importInvoiceService.createImport_Invoice(importInvoice));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PutMapping("/{id}")
    public BaseResponse<Import_InvoiceResponse> update(@PathVariable Long id, @Valid @RequestBody Import_Invoice importInvoice) {
        BaseResponse<Import_InvoiceResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(importInvoiceService.updateImport_Invoice(id, importInvoice));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteById(@PathVariable Long id) {
        importInvoiceService.deleteImport_Invoice(id);
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("Delete invoice successfully");
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }
}
