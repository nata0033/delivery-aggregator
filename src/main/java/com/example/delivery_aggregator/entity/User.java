package com.example.delivery_aggregator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class User {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String password;
}
