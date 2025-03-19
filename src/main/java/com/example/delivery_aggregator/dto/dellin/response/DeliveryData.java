package com.example.delivery_aggregator.dto.dellin.response;

import com.example.delivery_aggregator.dto.dellin.DellinCalculatorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryData {
    @JsonProperty("derival")
    private DerivalArrival derival;
    @JsonProperty("intercity")
    private CostsCalculation intercity;
    @JsonProperty("small")
    private CostsCalculation small;
    @JsonProperty("air")
    private CostsCalculation air;
    @JsonProperty("express")
    private CostsCalculation express;
    @JsonProperty("letter")
    private CostsCalculation letter;
    @JsonProperty("arrival")
    private DerivalArrival arrival;
    @JsonProperty("price")
    private String price;
    @JsonProperty("priceMinimal")
    private String priceMinimal;
    @JsonProperty("packages")
    private List<CostsCalculation> packages;
    @JsonProperty("orderDates")
    private OrderDates orderDates;
    @JsonProperty("deliveryTerm")
    private Integer deliveryTerm;
    @JsonProperty("accompanyingDocuments")
    private AcDoc accompanyingDocuments;
    @JsonProperty("insurance")
    private String insurance;
    @JsonProperty("insuranceComponents")
    private InsuranceComponents insuranceComponents;
    @JsonProperty("notify")
    private CostsCalculation notify;
    @JsonProperty("simpleShippingAvailable")
    private Boolean simpleShippingAvailable;
    @JsonProperty("availableDeliveryTypes")
    private AvailableDeliveryTypes availableDeliveryTypes;
    @JsonProperty("foundAddresses")
    private List<FoundAddress> foundAddresses;
    @JsonProperty("information")
    private List<String> information;
}

