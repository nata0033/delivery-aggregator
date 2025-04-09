package com.example.delivery_aggregator.dto.external_api.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FoundAddress {
    @JsonProperty("field")
    private String field;
    @JsonProperty("source")
    private String source;
    @JsonProperty("result")
    private String result;
}
