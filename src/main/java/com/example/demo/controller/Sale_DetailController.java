package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Sale_DetailResponse;
import com.example.demo.model.Sale_Detail;
import com.example.demo.service.Sale_DetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sale_detail")
public class Sale_DetailController {
    private Sale_DetailService saleDetailService;

    @GetMapping("/saleInvoice/{saleId}")
    public BaseResponse<List<Sale_DetailResponse>> getAllSaleDetailBySaleId(@PathVariable("saleId") Long saleId){
        BaseResponse<List<Sale_DetailResponse>>  baseResponse = new BaseResponse<>();
        baseResponse.setBody(saleDetailService.getSaleDetailBySaleId(saleId));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @GetMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> getSaleDetailById(@PathVariable("id") Long id){
        BaseResponse<Sale_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(saleDetailService.getSaleDetailById(id));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PostMapping("")
    public  BaseResponse<Sale_DetailResponse> createSaleDetail(@Valid @RequestBody Sale_Detail saleDetail){
        BaseResponse<Sale_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(saleDetailService.createSaleDetail(saleDetail));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PutMapping("/{id}")
    public BaseResponse<Sale_DetailResponse> updateSaleDetail(@PathVariable("id") Long id, @Valid @RequestBody Sale_Detail saleDetail){
        BaseResponse<Sale_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(saleDetailService.updateSaleDetail(id,saleDetail));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteSaleDetail(@PathVariable("id") Long id){
        saleDetailService.deleteSaleDetailById(id);
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("Delete Sale Detail Successfully");
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }
}
