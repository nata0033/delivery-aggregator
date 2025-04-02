package com.example.delivery_aggregator.dto.db;

import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.UUID;

public class UserDto {
    UUID id;
    String login;
    String password;
    private List<OrderDto> sentOrders;
    private List<ContactDto> contacts;
}
