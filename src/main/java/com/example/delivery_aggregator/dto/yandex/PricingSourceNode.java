package com.example.delivery_aggregator.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PricingSourceNode {
    @JsonProperty("platform_station_id")
    private String platformStationId;
}
