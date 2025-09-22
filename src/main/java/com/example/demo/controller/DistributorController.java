package com.example.demo.controller;

import com.example.demo.model.Distributor;
import com.example.demo.repo.DistributorRepo;
import com.example.demo.service.DistributorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/distributor")
public class DistributorController {
     DistributorService distributorService;
     ObjectMapper objectMapper;
     @GetMapping("")
     public List<Distributor> getAllDistributors() {
         return distributorService.findAllDistributors();
     }
     @GetMapping("/{id}")
     public Distributor getDistributorById(@PathVariable long id) {
         return distributorService.findDistributorById(id);
     }
     @PostMapping("")
        public Distributor createDistributor(@RequestBody Distributor distributor) {
         return distributorService.createDistributor(distributor);
     }
     @PutMapping("/{id}")
     public Distributor updateDistributor(@PathVariable long id, @RequestBody Distributor distributor) {
         return distributorService.updateDistributor(id, distributor);
     }
     @DeleteMapping("/{id}")
     String deleteDistributorById(@PathVariable long id) {
         distributorService.deleteDistributor(id);
         return "Distributor deleted successfully";
     }

}
