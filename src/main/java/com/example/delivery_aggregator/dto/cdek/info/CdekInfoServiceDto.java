package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoServiceDto {
    private String code;
    private String parameter;
    private Double sum;
    @JsonProperty("total_sum")
    private Double totalSum;
    @JsonProperty("discount_percent")
    private Double discountPercent;
    @JsonProperty("discount_sum")
    private Double discountSum;
    @JsonProperty("vat_rate")
    private Double vatRate;
    @JsonProperty("vat_sum")
    private Double vatSum;
}
