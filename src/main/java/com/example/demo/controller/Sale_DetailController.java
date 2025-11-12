package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Sale_DetailResponse;
import com.example.demo.model.Sale_Detail;
import com.example.demo.service.Sale_DetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sale_detail")
@Slf4j
public class Sale_DetailController {

    private final Sale_DetailService saleDetailService;

    @GetMapping("/saleInvoice/{saleId}")
    public BaseResponse<List<Sale_DetailResponse>> getAllSaleDetailBySaleId(@PathVariable("saleId") Long saleId) {
        log.info("Fetching all sale details for saleId: {}", saleId);
        List<Sale_DetailResponse> saleDetails = saleDetailService.getSaleDetailBySaleId(saleId);

        BaseResponse<List<Sale_DetailResponse>> response = new BaseResponse<>();
        response.setBody(saleDetails);
        response.setMessage("Fetched sale details successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> getSaleDetailById(@PathVariable("id") Long id) {
        log.info("Fetching sale detail by id: {}", id);
        Sale_DetailResponse saleDetail = saleDetailService.getSaleDetailById(id);

        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        response.setBody(saleDetail);
        response.setMessage("Fetched sale detail successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Sale_DetailResponse> createSaleDetail(@Valid @RequestBody Sale_Detail saleDetail) {
        log.info("Creating new sale detail for saleId: {}", saleDetail.getSaleId());
        Sale_DetailResponse created = saleDetailService.createSaleDetail(saleDetail);

        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        response.setBody(created);
        response.setMessage("Created sale detail successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> updateSaleDetail(
            @PathVariable("id") Long id,
            @Valid @RequestBody Sale_Detail saleDetail) {
        log.info("Updating sale detail with id: {}", id);
        Sale_DetailResponse updated = saleDetailService.updateSaleDetail(id, saleDetail);

        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        response.setBody(updated);
        response.setMessage("Updated sale detail successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleDetail(@PathVariable("id") Long id) {
        log.info("Deleting sale detail with id: {}", id);
        saleDetailService.deleteSaleDetailById(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Deleted sale detail successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
