package com.example.delivery_aggregator.dto.pochta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PochtaCalculatorRequest {
        @JsonProperty("completeness-checking")
        private Boolean completenessChecking;

        @JsonProperty("contents-checking")
        private Boolean contentsChecking;

        @JsonProperty("courier")
        private Boolean courier;

        @JsonProperty("declared-value")
        private Integer declaredValue;

        @JsonProperty("delivery-point-index")
        private String deliveryPointIndex;

        @JsonProperty("dimension")
        private Dimension dimension;

        @JsonProperty("dimension-type")
        private String dimensionType;

        @JsonProperty("entries-type")
        private String entriesType;

        @JsonProperty("fragile")
        private Boolean fragile;

        @JsonProperty("index-from")
        private String indexFrom;

        @JsonProperty("index-to")
        private String indexTo;

        @JsonProperty("inventory")
        private Boolean inventory;

        @JsonProperty("mail-category")
        private String mailCategory;

        @JsonProperty("mail-direct")
        private Integer mailDirect;

        @JsonProperty("mail-type")
        private String mailType;

        @JsonProperty("mass")
        private Integer mass;

        @JsonProperty("notice-payment-method")
        private String noticePaymentMethod;

        @JsonProperty("payment-method")
        private String paymentMethod;

        @JsonProperty("sms-notice-recipient")
        private Integer smsNoticeRecipient;

        @JsonProperty("transport-type")
        private String transportType;

        @JsonProperty("vsd")
        private Boolean vsd;

        @JsonProperty("with-electronic-notice")
        private Boolean withElectronicNotice;

        @JsonProperty("with-order-of-notice")
        private Boolean withOrderOfNotice;

        @JsonProperty("with-simple-notice")
        private Boolean withSimpleNotice;

}
