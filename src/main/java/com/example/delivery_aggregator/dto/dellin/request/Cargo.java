package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cargo {
    //private Integer quantity;
    private Float length;
    private Float width;
    private Float height;
    //private Float weight;
    private Float totalVolume;
    private Float totalWeight;
    //private Float oversizedWeight;
    //private Float oversizedVolume;
    //private String freightUID;
    private Float hazardClass;
    //private Insurance insurance;
}