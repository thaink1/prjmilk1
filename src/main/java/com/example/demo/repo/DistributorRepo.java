package com.example.demo.repo;

import com.example.demo.model.Category;
import com.example.demo.model.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributorRepo extends JpaRepository<Distributor, Long> {
    Optional<Distributor> getDistributorByDistributorId(Long distributorId);

}
