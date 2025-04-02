package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "package")
public class Package {

    @Id
    @GeneratedValue
    private UUID id;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
