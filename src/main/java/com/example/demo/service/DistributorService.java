package com.example.demo.service;

import com.example.demo.model.Distributor;
import com.example.demo.repo.DistributorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DistributorService {
    private DistributorRepo distributorRepo;
    public List<Distributor> findAllDistributors() {
        return distributorRepo.findAll();
    }
    public Distributor findDistributorById(Long id) {
        return distributorRepo.getDistributorByDistributorId(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));
    }
    public Distributor createDistributor(Distributor distributor) {
        if (!distributor.getPhone().matches("^0\\d{9}$")) {
            throw new RuntimeException("Phone number is not valid");
        }
        if (!distributor.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.com$")) {
            throw new RuntimeException("Email must be valid abc@example.com");
        }

        return distributorRepo.save(distributor);
    }
    public Distributor updateDistributor(Long id, Distributor distributor) {
        Distributor oldDistributor = distributorRepo.getDistributorByDistributorId(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));
        if (distributor.getPhone() != null && !distributor.getPhone().isEmpty()) {
            if (!distributor.getPhone().matches("^0\\d{9}$")) {
                throw new RuntimeException("Phone number is not valid");
            }
            oldDistributor.setPhone(distributor.getPhone());
        }
        if (distributor.getEmail() != null && !distributor.getEmail().isEmpty()) {
            if (!distributor.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.com$")) {
                throw new RuntimeException("Email must be valid abc@example.com");
            }
            oldDistributor.setEmail(distributor.getEmail());
        }
        if (distributor.getName() != null && !distributor.getName().isEmpty()) {
            oldDistributor.setName(distributor.getName());
        }
        if (distributor.getAddress() != null && !distributor.getAddress().isEmpty()) {
            oldDistributor.setAddress(distributor.getAddress());
        }
        return distributorRepo.save(oldDistributor);

    }
    public void deleteDistributor(Long id) {
        Distributor distributor = distributorRepo.getDistributorByDistributorId(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));
        distributorRepo.deleteById(distributor.getDistributorId());
    }
}
