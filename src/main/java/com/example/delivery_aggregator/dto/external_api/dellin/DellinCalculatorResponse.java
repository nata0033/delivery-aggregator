package com.example.delivery_aggregator.dto.external_api.dellin;

import com.example.delivery_aggregator.dto.external_api.dellin.response.DeliveryData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DellinCalculatorResponse {
    @JsonProperty("data")
    private DeliveryData deliveryData;
}