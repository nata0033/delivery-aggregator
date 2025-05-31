package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekOrderDeliveryRecipientCostDto {
    private Float value;
    @JsonProperty("vat_sum")
    private Float vatSum;
    @JsonProperty("vat_rate")
    private Integer vatRate;
}
