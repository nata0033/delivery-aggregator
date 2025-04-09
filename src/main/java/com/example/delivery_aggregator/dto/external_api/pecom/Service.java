package com.example.delivery_aggregator.dto.external_api.pecom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Service {
    @JsonProperty("key")
    private String key;

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("params")
    private List<Param> params;

    @JsonProperty("isChecked")
    private Boolean isChecked;
}
