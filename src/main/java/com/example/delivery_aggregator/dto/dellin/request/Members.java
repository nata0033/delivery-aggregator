package com.example.delivery_aggregator.dto.dellin.request;

import com.example.delivery_aggregator.dto.dellin.DellinCalculatorRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Members {
    @JsonProperty("requester")
    private Requester requester;
}
