package com.example.delivery_aggregator.dto.dellin;

import com.example.delivery_aggregator.dto.dellin.response.DeliveryData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DellinCalculatorResponse {
    @JsonProperty("data")
    private DeliveryData deliveryData;
}