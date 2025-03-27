package com.example.delivery_aggregator.service.api;

import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
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

    public ResponseEntity<DellinCalculatorResponse> getTariffs(IndexPageDataDto formDeliveryParams) {
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
//        dellinCalculatorTariffRequest.setAppkey("");
//        dellinCalculatorTariffRequest.setDelivery(delivery);
//        dellinCalculatorTariffRequest.setCargo(cargo);

        return restTemplate.postForEntity(REQUEST_URL, dellinCalculatorTariffRequest, DellinCalculatorResponse.class);
    }
}