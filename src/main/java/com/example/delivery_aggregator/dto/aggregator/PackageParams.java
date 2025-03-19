package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

@Data
public class PackageParams {
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
