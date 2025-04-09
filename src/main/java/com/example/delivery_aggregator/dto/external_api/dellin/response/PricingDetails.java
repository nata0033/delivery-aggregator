package com.example.delivery_aggregator.dto.external_api.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PricingDetails {
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private String value;
    @JsonProperty("date")
    private String date;
    @JsonProperty("announcement")
    private Boolean announcement;
    @JsonProperty("public")
    private Boolean isPublic;
    @JsonProperty("triggers")
    private List<Triggers> triggers;
}
