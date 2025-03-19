package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.aggregator.IndexPageData;
import com.example.delivery_aggregator.dto.dellin.DellinCalculatorRequest;
import com.example.delivery_aggregator.dto.dellin.DellinCalculatorResponse;
import com.example.delivery_aggregator.mappers.DellinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class DellinService {

    private final String URL = "https://api.dellin.ru";

    private final RestTemplate restTemplate;

    private final DellinMapper dellinMapper;

    public ResponseEntity<DellinCalculatorResponse> getTariffs(IndexPageData formDeliveryParams) {
        final String REQUEST_URL = URL + "/v2/calculator.json";

        DellinCalculatorRequest dellinCalculatorTariffRequest = dellinMapper.dellinCalculatorRequest(formDeliveryParams);
//        Delivery delivery = new Delivery();
//        Cargo cargo = dellinMapper.deliveryParamsToCargo(formDeliveryParams);
//        DeliveryType deliveryType = new DeliveryType();
//        Time time = new Time();
//        Address address = new Address();
//        DerivalArrival derival = new DerivalArrival();
//        DerivalArrival arrival = new DerivalArrival();
//
//        time.setWorktimeStart("08:00");
//        time.setWorktimeEnd("20:00");
//
//        derival.setProduceDate("2025-03-15");
//        derival.setVariant("address");
//        derival.setTime(time);
//        derival.setAddress(address);
//
//        arrival.setVariant("address");
//        arrival.setTime(time);
//        arrival.setAddress(address);
//
//        deliveryType.setType("auto");
//
//        delivery.setDeliveryType(deliveryType);
//        delivery.setDerival(derival);
//        delivery.setArrival(arrival);
//
//        dellinCalculatorTariffRequest.setAppkey("8EE8CB3E-587A-4EA5-8065-C076269D3F96");
//        dellinCalculatorTariffRequest.setDelivery(delivery);
//        dellinCalculatorTariffRequest.setCargo(cargo);

        return restTemplate.postForEntity(REQUEST_URL, dellinCalculatorTariffRequest, DellinCalculatorResponse.class);
    }
}