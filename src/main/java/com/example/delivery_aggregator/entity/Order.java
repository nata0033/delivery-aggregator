package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "order")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String serviceOrderNumber;

    @Column(nullable = false, columnDefinition = "VARCHAR(100) DEFAULT 'ACCEPTED'")
    private String status = "ACCEPTED";

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String fromLocation;

    @Column(nullable = false)
    private String toLocation;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipient_contact_id")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "delivery_service_id")
    private DeliveryService deliveryService;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "ACCEPTED";
        }
    }
}