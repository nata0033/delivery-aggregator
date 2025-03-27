package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phone;
    private String pic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;

    // Getters and Setters
}
