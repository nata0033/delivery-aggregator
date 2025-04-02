package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.DeliveryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryServiceRepository extends JpaRepository<DeliveryService, UUID> {
    Optional<DeliveryService> findByName(String name);
}
