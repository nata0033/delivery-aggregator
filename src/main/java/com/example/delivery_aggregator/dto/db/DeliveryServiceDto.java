package com.example.delivery_aggregator.dto.db;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeliveryServiceDto {
    private UUID id;
    private String name;
    private String logo;
    private List<OrderDto> orders;
}
