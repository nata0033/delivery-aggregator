package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DerivalArrival {
    @JsonProperty("produceDate")
    private String produceDate;
    @JsonProperty("variant")
    private String variant;
    //@JsonProperty("terminalID")
    //private String terminalID;
    //@JsonProperty("addressID")
    //private Integer addressID;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("time")
    private Time time;
    //@JsonProperty("handling")
    //private Handling handling;
    //@JsonProperty("requirements")
    //private List<String> requirements;
    //@JsonProperty("city")
    //private String city;
}
