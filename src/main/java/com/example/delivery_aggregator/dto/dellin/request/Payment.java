package com.example.delivery_aggregator.dto.dellin.request;

import com.example.delivery_aggregator.dto.dellin.DellinCalculatorRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Payment {
    private String type;
    private String promocode;
    private String paymentCity;
    private PaymentCitySearch paymentCitySearch;
}
