package com.example.delivery_aggregator.dto.external_api.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class YandexCalculatorRequest {
    private PricingSourceNode source;
    private PricingDestinationNode destination;
    private String tariff;
    @JsonProperty("total_weight")
    private Integer totalWeight;
    @JsonProperty("total_assessed_price")
    private Integer totalAssessedPrice;
    @JsonProperty("client_price")
    private Integer clientPrice;
    @JsonProperty("payment_method")
    private String paymentMethod;
    private List<PricingResourcePlace> places;
}