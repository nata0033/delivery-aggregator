package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

@Data
public class PackageParamsDto {
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
