package com.example.delivery_aggregator.dto.external_api.cdek.calculator;

import lombok.Data;

@Data
public class CdekCalculatorPackageDto {
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
