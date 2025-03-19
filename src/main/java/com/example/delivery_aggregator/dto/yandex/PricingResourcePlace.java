package com.example.delivery_aggregator.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PricingResourcePlace {
    @JsonProperty("physical_dims")
    private PlacePhysicalDimensions physicalDims;
}
