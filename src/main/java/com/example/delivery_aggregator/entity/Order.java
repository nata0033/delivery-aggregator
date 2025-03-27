package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private String serviceName;
    private String serviceLogo;
    private String number;
    private Date date;
    private String status;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
}
