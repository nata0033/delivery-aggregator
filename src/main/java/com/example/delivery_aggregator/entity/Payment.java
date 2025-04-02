package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "payment")
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    private String externalPaymentId;

    private Float amount;

    @Column(length = 3, columnDefinition = "VARCHAR(3) DEFAULT 'RUB'")
    private String currency = "RUB";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @PrePersist
    public void prePersist() {
        if (currency == null) {
            currency = "RUB";
        }
    }
}