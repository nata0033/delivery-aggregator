package com.example.delivery_aggregator.dto.cdek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekPostcodesResponseDto {
    private  Integer code;
    @JsonProperty("postal_codes")
    private List<String> postalCodes;
}
