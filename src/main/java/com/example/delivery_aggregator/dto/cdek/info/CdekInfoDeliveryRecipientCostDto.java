package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoDeliveryRecipientCostDto {
    private Double value;
    @JsonProperty("vat_sum")
    private Double vatSum;
    @JsonProperty("vat_rate")
    private Integer vatRate;
}
