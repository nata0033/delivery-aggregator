package com.example.delivery_aggregator.dto.external_api.pochta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rate {

    @JsonProperty("rate")
    private Integer rate;

    @JsonProperty("vat")
    private Integer vat;
}