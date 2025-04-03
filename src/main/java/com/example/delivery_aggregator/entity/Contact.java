package com.example.delivery_aggregator.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "contact")
public class Contact {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;

    @Column(columnDefinition = "TEXT DEFAULT 'https://i.pinimg.com/736x/97/55/6b/97556b3f5865b4dc1c3aece334c0eeac.jpg'")
    private String pic = "https://i.pinimg.com/736x/97/55/6b/97556b3f5865b4dc1c3aece334c0eeac.jpg";

    @Column(length = 20)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;

    @OneToMany(mappedBy = "contact")
    private List<Order> receivedOrders;

    @PrePersist
    public void prePersist() {
        if (pic == null) {
            pic = "https://i.pinimg.com/736x/97/55/6b/97556b3f5865b4dc1c3aece334c0eeac.jpg";
        }
    }
}