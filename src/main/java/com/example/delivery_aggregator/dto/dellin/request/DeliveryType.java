package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryType {
    @JsonProperty("type")
    private String type;
}