package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    private String country;
    private String state;
    private String city;
    private String house;
    private String apartment;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    // Getters and Setters
}