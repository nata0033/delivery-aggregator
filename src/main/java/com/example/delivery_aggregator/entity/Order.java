package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String fromLocation;

    @Column(nullable = false)
    private String toLocation;

    @Column(name = "last_update", nullable = false)
    @UpdateTimestamp  // Автоматическое обновление при изменении сущности
    private LocalDateTime lastUpdate;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
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

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<Package> packages;
}