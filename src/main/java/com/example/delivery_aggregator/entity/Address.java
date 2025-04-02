package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "address")
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) DEFAULT 'Россия'")
    private String country;

    private String state;

    private String city;

    @Column(length = 20)
    private String house;

    @Column(length = 20)
    private String apartment;

    @Column(length = 20)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
}