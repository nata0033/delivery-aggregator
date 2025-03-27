package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "user")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String login;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    // Getters and Setters
}