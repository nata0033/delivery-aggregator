package com.example.delivery_aggregator.dto.external_api.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcDoc {
    @JsonProperty("send")
    private CostsCalculation send;
    @JsonProperty("return")
    private CostsCalculation returnDoc;
}
