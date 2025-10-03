package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Distributor;
import com.example.demo.repo.DistributorRepo;
import com.example.demo.service.DistributorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/distributor")
public class DistributorController {
     DistributorService distributorService;
     ObjectMapper objectMapper;
     @GetMapping("")
     BaseResponse<List<Distributor>> getAllDistributors() {
         BaseResponse<List<Distributor>> response = new BaseResponse<>();
         response.setBody(distributorService.findAllDistributors());
         response.setRequestId(MDC.get("requestId"));
         response.setResponseTime(LocalDateTime.now());
         return response;
     }

     @GetMapping("/{id}")
     BaseResponse<Distributor> getDistributorById(@PathVariable long id) {
         BaseResponse<Distributor> response = new BaseResponse<>();
         response.setBody(distributorService.findDistributorById(id));
         response.setRequestId(MDC.get("requestId"));
         response.setResponseTime(LocalDateTime.now());
         return response;
     }

     @PostMapping("")
        BaseResponse<Distributor> createDistributor(@Valid  @RequestBody Distributor distributor) {
         BaseResponse<Distributor> response = new BaseResponse<>();
         response.setBody(distributorService.createDistributor(distributor));
         response.setRequestId(MDC.get("requestId"));
         response.setResponseTime(LocalDateTime.now());
         return response;
     }

     @PutMapping("/{id}")
     BaseResponse<Distributor> updateDistributor(@PathVariable long id, @Valid @RequestBody Distributor distributor) {
        BaseResponse<Distributor> response = new BaseResponse<>();
        response.setBody(distributorService.updateDistributor(id, distributor));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
     }

     @DeleteMapping("/{id}")
     BaseResponse<Void> deleteDistributorById(@PathVariable long id) {
         distributorService.deleteDistributor(id);
         BaseResponse<Void> response = new BaseResponse<>();
         response.setMessage("Successfully deleted distributor");
         response.setRequestId(MDC.get("requestId"));
         response.setResponseTime(LocalDateTime.now());
         return response;
     }

}
