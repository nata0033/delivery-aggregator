package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CdekInfoDeliveryDetailDto {
    private LocalDate date;
    @JsonProperty("recipient_name")
    private String recipientName;
    @JsonProperty("payment_sum")
    private Double paymentSum;
    @JsonProperty("delivery_sum")
    private Double deliverySum;
    @JsonProperty("total_sum")
    private Double totalSum;
    @JsonProperty("payment_info")
    private List<CdekInfoPaymentInfoDto> paymentInfo;
    @JsonProperty("delivery_vat_rate")
    private Double deliveryVatRate;
    @JsonProperty("delivery_vat_sum")
    private Double deliveryVatSum;
    @JsonProperty("delivery_discount_percent")
    private Double deliveryDiscountPercent;
    @JsonProperty("delivery_discount_sum")
    private Double deliveryDiscountSum;
}
