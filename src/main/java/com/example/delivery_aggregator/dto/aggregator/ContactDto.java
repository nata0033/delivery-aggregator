package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.UUID;

@Data
public class ContactDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phone;
    private String pic;
}
