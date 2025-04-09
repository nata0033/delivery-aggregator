package com.example.delivery_aggregator.dto.external_api.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Delivery {
    @JsonProperty("deliveryType")
    private DeliveryType deliveryType;
    @JsonProperty("arrival")
    private DerivalArrival arrival;
    @JsonProperty("derival")
    private DerivalArrival derival;
    //@JsonProperty("packages")
    //private List<Packages> packages;
    //@JsonProperty("accompanyingDocuments")
    //private List<AcDoc> accompanyingDocuments;
}