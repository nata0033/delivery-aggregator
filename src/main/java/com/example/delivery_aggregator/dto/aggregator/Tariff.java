package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

@Data
public class Tariff {
    private Integer code;
    private String name;
    private Integer minTime;
    private Integer maxTime;
    private Integer price;
}
