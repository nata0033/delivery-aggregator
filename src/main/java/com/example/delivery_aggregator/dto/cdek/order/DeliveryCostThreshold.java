package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryCostThreshold {
    private Integer threshold;
    private Float sum;
    @JsonProperty("vat_sum")
    private Float vatSum;
    @JsonProperty("vat_rate")
    private Integer vatRate;
}
