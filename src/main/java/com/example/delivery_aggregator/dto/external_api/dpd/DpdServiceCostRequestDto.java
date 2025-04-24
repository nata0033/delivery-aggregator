package com.example.delivery_aggregator.dto.external_api.dpd;

import lombok.Data;

import java.util.Date;

@Data
public class DpdServiceCostRequestDto {
    private DpdAuthDto auth;
    private DpdLocationDto pickup;
    private DpdLocationDto delivery;
    private Boolean selfPickup;
    private Boolean selfDelivery;
    private Double weight;
    private Double volume;
    private String serviceCode;
    private Date pickupDate;
    private Integer maxDays;
    private Double maxCost;
    private Double declaredValue;
}
