package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
import java.util.List;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "delivery_service")
public class DeliveryService {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String logo;

    @OneToMany(mappedBy = "deliveryService")
    private List<Order> orders;
}