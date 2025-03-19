package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Insurance {
    @JsonProperty("statedValue")
    private Double statedValue;
    @JsonProperty("term")
    private Boolean term;
}
