package com.example.delivery_aggregator.dto.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Triggers {
    @JsonProperty("key")
    private String key;
}