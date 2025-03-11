package com.example.delivery_aggregator.dto;

import lombok.Data;

@Data
public class Package {
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
