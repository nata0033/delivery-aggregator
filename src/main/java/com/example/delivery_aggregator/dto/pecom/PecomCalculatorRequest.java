package com.example.delivery_aggregator.dto.pecom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PecomCalculatorRequest {
    @JsonProperty("totalWeight")
    private Double totalWeight;

    @JsonProperty("totalVolume")
    private Double totalVolume;

    @JsonProperty("maxDimension")
    private Double maxDimension;

    @JsonProperty("isOnePlace")
    private Integer isOnePlace;

    @JsonProperty("cargoPlaces")
    private List<CargoPlace> cargoPlaces;

    @JsonProperty("services")
    private List<Service> services;

    @JsonProperty("calculationCurrencyCode")
    private Integer calculationCurrencyCode;
}
