package com.example.delivery_aggregator.dto.dellin.response;

import com.example.delivery_aggregator.dto.dellin.DellinCalculatorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CostsCalculation {
    @JsonProperty("price")
    private String price;
    @JsonProperty("contractPrice")
    private Boolean contractPrice;
    @JsonProperty("premium")
    private String premium;
    @JsonProperty("discount")
    private String discount;
    @JsonProperty("premiumDetails")
    private List<PricingDetails> premiumDetails;
    @JsonProperty("discountDetails")
    private List<PricingDetails> discountDetails;
}
