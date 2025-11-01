package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Import_DetailResponse;
import com.example.demo.model.Import_Detail;
import com.example.demo.service.Import_DetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/import_detail")
@AllArgsConstructor
@Slf4j
public class Import_DetailController {

    private final Import_DetailService import_DetailService;

    @GetMapping("/invoice/{importId}")
    public BaseResponse<List<Import_DetailResponse>> getDetailByImportId(@PathVariable long importId) {
        BaseResponse<List<Import_DetailResponse>> response = new BaseResponse<>();
        try {
            log.info("Fetching import details by importId: {}", importId);
            response.setBody(import_DetailService.getDetailByImportId(importId));
            response.setMessage("Fetched details successfully");
        } catch (Exception e) {
            log.error("Error fetching details for importId: {}", importId, e);
            response.setMessage("Failed to fetch details: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{detailId}")
    public BaseResponse<Import_DetailResponse> getDetailById(@PathVariable long detailId) {
        BaseResponse<Import_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Fetching import detail by ID: {}", detailId);
            response.setBody(import_DetailService.getDetailById(detailId));
            response.setMessage("Fetched detail successfully");
        } catch (Exception e) {
            log.error("Error fetching import detail with ID: {}", detailId, e);
            response.setMessage("Failed to fetch detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Import_DetailResponse> createDetail(@Valid @RequestBody Import_Detail importDetail) {
        BaseResponse<Import_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Creating new import detail for importId: {}", importDetail.getImportId());
            response.setBody(import_DetailService.createDetail(importDetail));
            response.setMessage("Import detail created successfully");
        } catch (Exception e) {
            log.error("Error creating import detail", e);
            response.setMessage("Failed to create import detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{detailId}")
    public BaseResponse<Import_DetailResponse> updateDetail(
            @PathVariable long detailId,
            @Valid @RequestBody Import_Detail importDetail) {
        BaseResponse<Import_DetailResponse> response = new BaseResponse<>();
        try {
            log.info("Updating import detail ID: {}", detailId);
            response.setBody(import_DetailService.updateDetail(detailId, importDetail));
            response.setMessage("Import detail updated successfully");
        } catch (Exception e) {
            log.error("Error updating import detail ID: {}", detailId, e);
            response.setMessage("Failed to update import detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{detailId}")
    public BaseResponse<Void> deleteDetail(@PathVariable long detailId) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Deleting import detail with ID: {}", detailId);
            import_DetailService.deleteDetailById(detailId);
            response.setMessage("Import detail deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting import detail with ID: {}", detailId, e);
            response.setMessage("Failed to delete import detail: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
