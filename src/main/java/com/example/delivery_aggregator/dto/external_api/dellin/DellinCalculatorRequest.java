package com.example.delivery_aggregator.dto.external_api.dellin;

import com.example.delivery_aggregator.dto.external_api.dellin.request.Cargo;
import com.example.delivery_aggregator.dto.external_api.dellin.request.Delivery;
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