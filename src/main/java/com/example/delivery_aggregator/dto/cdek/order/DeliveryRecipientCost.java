package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryRecipientCost {
    private Double value;
    @JsonProperty("vat_sum")
    private Double vatSum;
    @JsonProperty("vat_rate")
    private Integer vatRate;
}
