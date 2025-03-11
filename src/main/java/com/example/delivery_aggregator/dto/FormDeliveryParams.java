package com.example.delivery_aggregator.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDeliveryParams {
    private String fromLocation;
    private String toLocation;
    private List<Package> packages;
}
