package com.example.delivery_aggregator.dto.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Terminal {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("streetCode")
    private String streetCode;
    @JsonProperty("price")
    private String price;
    @JsonProperty("contractPrice")
    private Boolean contractPrice;
    @JsonProperty("default")
    private Boolean defaultTerminal;
    @JsonProperty("express")
    private Boolean express;
    @JsonProperty("isPVZ")
    private Boolean isPVZ;
}