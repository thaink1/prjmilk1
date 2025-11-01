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
        BaseResponse<List<Sale_DetailResponse>> response = new BaseResponse<>();
        try {
            log.info("Fetching all sale details for saleId: {}", saleId);
            response.setBody(saleDetailService.getSaleDetailBySaleId(saleId));
            response.setMessage("Fetched sale details successfully");
        } catch (Exception e) {
            log.error("Error fetching sale details for saleId: {}", saleId, e);
            response.setMessage("Failed to fetch sale details: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> getSaleDetailById(@PathVariable("id") Long id) {
        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Fetching sale detail by id: {}", id);
            response.setBody(saleDetailService.getSaleDetailById(id));
            response.setMessage("Fetched sale detail successfully");
        } catch (Exception e) {
            log.error("Error fetching sale detail by id: {}", id, e);
            response.setMessage("Failed to fetch sale detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Sale_DetailResponse> createSaleDetail(@Valid @RequestBody Sale_Detail saleDetail) {
        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Creating new sale detail for saleId: {}", saleDetail.getSaleId());
            response.setBody(saleDetailService.createSaleDetail(saleDetail));
            response.setMessage("Created sale detail successfully");
        } catch (Exception e) {
            log.error("Error creating sale detail for saleId: {}", saleDetail.getSaleId(), e);
            response.setMessage("Failed to create sale detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> updateSaleDetail(
            @PathVariable("id") Long id,
            @Valid @RequestBody Sale_Detail saleDetail) {
        BaseResponse<Sale_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Updating sale detail with id: {}", id);
            response.setBody(saleDetailService.updateSaleDetail(id, saleDetail));
            response.setMessage("Updated sale detail successfully");
        } catch (Exception e) {
            log.error("Error updating sale detail with id: {}", id, e);
            response.setMessage("Failed to update sale detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleDetail(@PathVariable("id") Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Deleting sale detail with id: {}", id);
            saleDetailService.deleteSaleDetailById(id);
            response.setMessage("Deleted sale detail successfully");
        } catch (Exception e) {
            log.error("Error deleting sale detail with id: {}", id, e);
            response.setMessage("Failed to delete sale detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
