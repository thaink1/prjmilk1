package com.example.demo.service;

import com.example.demo.model.Distributor;
import com.example.demo.repo.DistributorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DistributorService {

    private final DistributorRepo distributorRepo;

    public List<Distributor> findAllDistributors() {
        try {
            log.info("Fetching all distributors...");
            List<Distributor> distributors = distributorRepo.findAll();
            log.info("Fetched {} distributors successfully", distributors.size());
            return distributors;
        } catch (Exception e) {
            log.error("Error while fetching all distributors", e);
            throw e;
        }
    }

    public Distributor findDistributorById(Long id) {
        try {
            log.info("Fetching distributor with ID: {}", id);
            Distributor distributor = distributorRepo.getDistributorByDistributorId(id)
                    .orElseThrow(() -> {
                        log.warn("Distributor not found with ID: {}", id);
                        return new RuntimeException("Distributor not found");
                    });
            log.info("Fetched distributor successfully: {}", distributor.getName());
            return distributor;
        } catch (Exception e) {
            log.error("Error while fetching distributor with ID: {}", id, e);
            throw e;
        }
    }

    public Distributor createDistributor(Distributor distributor) {
        try {
            log.info("Creating new distributor: {}", distributor.getName());
            Distributor saved = distributorRepo.save(distributor);
            log.info("Distributor created successfully: {}", saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Error while creating distributor: {}", distributor.getName(), e);
            throw e;
        }
    }

    public Distributor updateDistributor(Long id, Distributor distributor) {
        try {
            log.info("Updating distributor with ID: {}", id);
            Distributor existing = distributorRepo.getDistributorByDistributorId(id)
                    .orElseThrow(() -> {
                        log.warn("Distributor not found with ID: {}", id);
                        return new RuntimeException("Distributor not found");
                    });

            existing.setPhone(distributor.getPhone());
            existing.setEmail(distributor.getEmail());
            existing.setName(distributor.getName());
            existing.setAddress(distributor.getAddress());

            Distributor updated = distributorRepo.save(existing);
            log.info("Distributor updated successfully: {}", updated.getName());
            return updated;
        } catch (Exception e) {
            log.error("Error while updating distributor with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteDistributor(Long id) {
        try {
            log.info("Deleting distributor with ID: {}", id);
            Distributor distributor = distributorRepo.getDistributorByDistributorId(id)
                    .orElseThrow(() -> {
                        log.warn("Distributor not found with ID: {}", id);
                        return new RuntimeException("Distributor not found");
                    });
            distributorRepo.deleteById(distributor.getDistributorId());
            log.info("Distributor deleted successfully: {}", distributor.getName());
        } catch (Exception e) {
            log.error("Error while deleting distributor with ID: {}", id, e);
            throw e;
        }
    }
}
