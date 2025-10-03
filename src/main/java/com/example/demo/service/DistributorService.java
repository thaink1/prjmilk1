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
        return distributorRepo.save(distributor);
    }
    public Distributor updateDistributor(Long id, Distributor distributor) {
        Distributor oldDistributor = distributorRepo.getDistributorByDistributorId(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));
            oldDistributor.setPhone(distributor.getPhone());
            oldDistributor.setEmail(distributor.getEmail());
            oldDistributor.setName(distributor.getName());
            oldDistributor.setAddress(distributor.getAddress());
        return distributorRepo.save(oldDistributor);

    }
    public void deleteDistributor(Long id) {
        Distributor distributor = distributorRepo.getDistributorByDistributorId(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));
        distributorRepo.deleteById(distributor.getDistributorId());
    }
}
