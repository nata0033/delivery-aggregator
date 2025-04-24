package com.example.delivery_aggregator.dto.external_api.dpd;

import lombok.Data;

@Data
public class DpdServiceCostResponseDto {
    private String serviceСode;
    private String serviceName;
    private Double cost;
    private Integer days;
    private Double weight;
    private Double volume;
}
