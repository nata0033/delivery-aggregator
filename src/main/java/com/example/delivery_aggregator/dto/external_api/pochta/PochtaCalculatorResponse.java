package com.example.delivery_aggregator.dto.external_api.pochta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PochtaCalculatorResponse {

        @JsonProperty("avia-rate")
        private Rate aviaRate;

        @JsonProperty("completeness-checking-rate")
        private Rate completenessCheckingRate;

        @JsonProperty("contents-checking-rate")
        private Rate contentsCheckingRate;

        @JsonProperty("delivery-time")
        private DeliveryTime deliveryTime;

        @JsonProperty("fragile-rate")
        private Rate fragileRate;

        @JsonProperty("ground-rate")
        private Rate groundRate;

        @JsonProperty("insurance-rate")
        private Rate insuranceRate;

        @JsonProperty("inventory-rate")
        private Rate inventoryRate;

        @JsonProperty("notice-payment-method")
        private String noticePaymentMethod;

        @JsonProperty("notice-rate")
        private Rate noticeRate;

        @JsonProperty("oversize-rate")
        private Rate oversizeRate;

        @JsonProperty("payment-method")
        private String paymentMethod;

        @JsonProperty("sms-notice-recipient-rate")
        private Rate smsNoticeRecipientRate;

        @JsonProperty("total-rate")
        private Integer totalRate;

        @JsonProperty("total-vat")
        private Integer totalVat;

        @JsonProperty("vsd-rate")
        private Rate vsdRate;
}
