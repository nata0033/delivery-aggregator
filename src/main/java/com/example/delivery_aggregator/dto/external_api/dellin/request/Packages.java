package com.example.delivery_aggregator.dto.external_api.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Packages {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("count")
    private Integer count;
}
