package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Import_DetailResponse;
import com.example.demo.model.Import_Detail;
import com.example.demo.service.Import_DetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("import_detail")
@AllArgsConstructor
public class Import_DetailController {
    private Import_DetailService import_DetailService;
    @GetMapping("/invoice/{importid}")
    public BaseResponse<List<Import_DetailResponse>> getDetailByImportId(@PathVariable long importid){
        BaseResponse<List<Import_DetailResponse>> baseResponse = new BaseResponse<>();
        baseResponse.setBody(import_DetailService.getDetailByImportId(importid));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @GetMapping("/{detailId}")
    public BaseResponse<Import_DetailResponse> getDetailById(@PathVariable long detailId){
        BaseResponse<Import_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(import_DetailService.getDetailById(detailId));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PostMapping("")
    public BaseResponse<Import_DetailResponse> createDetail(@Valid @RequestBody Import_Detail import_Detail){
        BaseResponse<Import_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(import_DetailService.createDetail(import_Detail));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @PutMapping("/{detailId}")
    public BaseResponse<Import_DetailResponse> updateDetail(@Valid @PathVariable long detailId,@RequestBody Import_Detail import_Detail){
        BaseResponse<Import_DetailResponse> baseResponse = new BaseResponse<>();
        baseResponse.setBody(import_DetailService.updateDetail(detailId,import_Detail));
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }

    @DeleteMapping("/{detailId}")
    public BaseResponse<Void> deleteDetail(@PathVariable long detailId){
        import_DetailService.deleteDetailById(detailId);
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("Delete Detail successfully");
        baseResponse.setRequestId(MDC.get("requestId"));
        baseResponse.setResponseTime(LocalDateTime.now());
        return baseResponse;
    }
}
