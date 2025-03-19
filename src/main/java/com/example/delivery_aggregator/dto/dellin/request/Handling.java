package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Handling {
    @JsonProperty("freightLift")
    private Boolean freightLift;
    @JsonProperty("toFloor")
    private Integer toFloor;
    @JsonProperty("carry")
    private Integer carry;
}
