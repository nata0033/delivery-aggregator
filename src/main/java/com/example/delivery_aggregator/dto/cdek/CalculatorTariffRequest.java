package com.example.delivery_aggregator.dto.cdek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CalculatorTariffRequest {
    private String date;
    private Integer type;
    @JsonProperty("additional_order_types")
    private List<Integer> additionalOrderTypes;
    private Integer currency;
    private String lang;
    @JsonProperty("from_location")
    private Location fromLocation;
    @JsonProperty("to_location")
    private Location toLocation;
    private List<com.example.delivery_aggregator.dto.Package> packages;
}
