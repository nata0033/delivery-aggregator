package com.example.delivery_aggregator.dto.cdek.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {
    private Integer code;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("country_code")
    private String countryCode;
    private String city;
    private String address;
    @JsonProperty("contragent_type")
    private String contragentType;
}
