package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryData {
    private Location fromLocation;
    private Location toLocation;
    private List<Package> packages;
    private Tariff tariff;
}
