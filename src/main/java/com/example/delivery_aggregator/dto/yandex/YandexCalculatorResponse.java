package com.example.delivery_aggregator.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YandexCalculatorResponse {
    private Boolean error;

    @JsonProperty("pricing_total")
    private String pricingTotal;

    @JsonProperty("pricing_commission_on_delivery_payment_amount")
    private String pricingCommissionOnDeliveryPaymentAmount;

    @JsonProperty("pricing_commission_on_delivery_payment")
    private String pricingCommissionOnDeliveryPayment;

    private String pricing;

    private String message;
}
