package com.example.delivery_aggregator.entity.response;

import lombok.Data;

@Data
public class Package {
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
