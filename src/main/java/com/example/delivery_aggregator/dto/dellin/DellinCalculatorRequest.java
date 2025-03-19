package com.example.delivery_aggregator.dto.dellin;

import com.example.delivery_aggregator.dto.dellin.request.Cargo;
import com.example.delivery_aggregator.dto.dellin.request.Delivery;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DellinCalculatorRequest {

    private String appkey;
    //private String sessionID;
    private Delivery delivery;
    //private Members members;
    private Cargo cargo;
    //private Payment payment;
}