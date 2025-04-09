package com.example.delivery_aggregator.dto.external_api.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DerivalArrival {
    @JsonProperty("terminal")
    private String terminal;
    @JsonProperty("price")
    private String price;
    @JsonProperty("contractPrice")
    private Boolean contractPrice;
    @JsonProperty("servicePrice")
    private String servicePrice;
    @JsonProperty("premiumDetails")
    private List<PricingDetails> premiumDetails;
    @JsonProperty("discountDetails")
    private List<PricingDetails> discountDetails;
    @JsonProperty("terminals")
    private List<Terminal> terminals;
    @JsonProperty("handling")
    private CostsCalculation handling;
}
