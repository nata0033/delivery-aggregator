package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

@Data
public class TariffDto {
    private DeliveryServiceDto service;
    private String code;
    private String name;
    private Integer minTime;
    private Integer maxTime;
    private Integer price;
}
