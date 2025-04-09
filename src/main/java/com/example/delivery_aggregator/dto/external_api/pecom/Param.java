package com.example.delivery_aggregator.dto.external_api.pecom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Param {
    @JsonProperty("key")
    private String key;

    @JsonProperty("values")
    private Integer values;
}
