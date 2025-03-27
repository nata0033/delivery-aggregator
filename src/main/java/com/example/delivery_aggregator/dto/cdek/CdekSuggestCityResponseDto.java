package com.example.delivery_aggregator.dto.cdek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekSuggestCityResponseDto {
    @JsonProperty("city_uuid")
    private String cityUuid;
    private Integer code;
    @JsonProperty("full_name")
    private String fullName;
}
