package com.example.delivery_aggregator.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryParams {
    private String fromLocation;
    private String toLocation;
    private List<Package> packages;
}
