package com.example.delivery_aggregator.dto.external_api.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlacePhysicalDimensions {
    @JsonProperty("weight_gross")
    private Integer weightGross;
    private Integer dx;
    private Integer dy;
    private Integer dz;
    @JsonProperty("predefined_volume")
    private Integer predefinedVolume;
}