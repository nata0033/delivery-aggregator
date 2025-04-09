package com.example.delivery_aggregator.dto.external_api.cdek.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekCalculatorRequestDto {
    private String date;
    private Integer type;
    @JsonProperty("additional_order_types")
    private List<Integer> additionalOrderTypes;
    private Integer currency;
    private String lang;
    @JsonProperty("from_location")
    private CdekCalculatorLocationDto fromLocation;
    @JsonProperty("to_location")
    private CdekCalculatorLocationDto toLocation;
    private List<CdekCalculatorPackageDto> packages;
}