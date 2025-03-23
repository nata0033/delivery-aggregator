package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private String serviceName;
    private String serviceLogo;
    private String number;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
}
