package com.example.delivery_aggregator.dto.db;

import java.util.List;
import java.util.UUID;

public class UserDto {
    UUID id;
    String login;
    String password;
    private List<OrderDto> sentOrders;
    private List<ContactDto> contacts;
}
