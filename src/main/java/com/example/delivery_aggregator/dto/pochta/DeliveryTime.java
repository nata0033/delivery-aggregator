package com.example.delivery_aggregator.dto.pochta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryTime {

    @JsonProperty("max-days")
    private Integer maxDays;

    @JsonProperty("min-days")
    private Integer minDays;
}