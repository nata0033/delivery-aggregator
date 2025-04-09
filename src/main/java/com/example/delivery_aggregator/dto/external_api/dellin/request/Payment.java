package com.example.delivery_aggregator.dto.external_api.dellin.request;

import lombok.Data;

@Data
public class Payment {
    private String type;
    private String promocode;
    private String paymentCity;
    private PaymentCitySearch paymentCitySearch;
}
