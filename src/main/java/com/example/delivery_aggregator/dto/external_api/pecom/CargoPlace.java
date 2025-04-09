package com.example.delivery_aggregator.dto.external_api.pecom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CargoPlace {
    @JsonProperty("placeNumber")
    private Integer placeNumber;

    @JsonProperty("width")
    private Double width;

    @JsonProperty("length")
    private Double length;

    @JsonProperty("height")
    private Double height;

    @JsonProperty("volume")
    private Double volume;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("countPlaces")
    private Integer countPlaces;

    @JsonProperty("services")
    private List<Service> services;

    @JsonProperty("isOverSized")
    private Integer isOverSized;

    @JsonProperty("barCode")
    private String barCode;
}
