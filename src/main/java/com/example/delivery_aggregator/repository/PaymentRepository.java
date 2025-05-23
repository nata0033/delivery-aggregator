package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
