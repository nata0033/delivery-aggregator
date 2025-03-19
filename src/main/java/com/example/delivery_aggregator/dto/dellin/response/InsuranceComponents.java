package com.example.delivery_aggregator.dto.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsuranceComponents {
    @JsonProperty("cargoInsurance")
    private String cargoInsurance;
    @JsonProperty("termInsurance")
    private String termInsurance;
    @JsonProperty("contractPrice")
    private Boolean contractPrice;
}
