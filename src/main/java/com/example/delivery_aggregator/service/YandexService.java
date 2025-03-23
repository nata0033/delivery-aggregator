package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.aggregator.IndexPageData;
import com.example.delivery_aggregator.dto.aggregator.Package;
import com.example.delivery_aggregator.dto.yandex.*;
import com.example.delivery_aggregator.mappers.YandexMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@AllArgsConstructor
@Service
public class YandexService {
    private static final String URL = "https://b2b.taxi.tst.yandex.net";

    private final YandexMapper yandexMapper;

    private final RestTemplate restTemplate;

    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("");
        headers.set("Accept-Language","ru");
        return headers;
    }

    public ResponseEntity<YandexCalculatorResponse> getTariffs(IndexPageData deliveryParams) {
        final String REQUEST_URL = URL + "/api/b2b/platform/pricing-calculator?is_oversized={is_oversized}";

        PricingDestinationNode pricingDestinationNode = new PricingDestinationNode();
        //pricingDestinationNode.setPlatformStationId("fbed3aa1-2cc6-4370-ab4d-59c5cc9bb924");
        pricingDestinationNode.setAddress("Санкт-Петербург, Большая Монетная улица, 1к1А");

        PricingSourceNode pricingSourceNode = new PricingSourceNode();
        //pricingSourceNode.setPlatformStationId("fbed3aa1-2cc6-4370-ab4d-59c5cc9bb924");

        YandexCalculatorRequest yandexCalculatorRequest = new YandexCalculatorRequest();
        yandexCalculatorRequest.setPlaces(deliveryParams.getPackages().stream().map(yandexMapper::packageToPricingResourcePlace).toList());
        //yandexCalculatorRequest.setDestination(yandexMapper.locationToPricingDestinationNode(deliveryParams.getToLocation()));
        yandexCalculatorRequest.setDestination(pricingDestinationNode);
        yandexCalculatorRequest.setSource(pricingSourceNode);
        yandexCalculatorRequest.setTariff("self_pickup");
        int total_weight = 0;
        for (Package p : deliveryParams.getPackages()) {
            int weight = p.getPackageParams().getWeight();
            total_weight += weight;
        }
        yandexCalculatorRequest.setTotalWeight(total_weight);

        HttpHeaders headers = getHttpHeaders();
        HttpEntity<YandexCalculatorRequest> requestData = new HttpEntity<>(yandexCalculatorRequest, headers);
        Boolean is_oversized = false;
        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, YandexCalculatorResponse.class, is_oversized);
    }
}