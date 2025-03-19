package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Packages {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("count")
    private Integer count;
}
