package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class TariffsPageData {
    private DeliveryService service;
    private Location fromLocation;
    private Location toLocation;
    private List<Tariff> tariffs;
    private Integer packageQuantity;
    private Integer packageWeight;
}
