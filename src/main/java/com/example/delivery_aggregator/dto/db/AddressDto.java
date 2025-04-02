package com.example.delivery_aggregator.dto.db;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressDto {
    private UUID id;
    private String country;
    private String state;
    private String city;
    private String house;
    private String apartment;
    private String postalCode;
}
