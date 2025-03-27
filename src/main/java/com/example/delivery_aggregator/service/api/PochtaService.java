package com.example.delivery_aggregator.service.api;

import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pochta.PochtaCalculatorResponse;
import com.example.delivery_aggregator.dto.pochta.PochtaCalculatorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class PochtaService {

    final String URL = "https://otpravka-api.pochta.ru";

    private final RestTemplate restTemplate;

    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "AccessToken AccessToken sDBaa9XNfFargSyQ8KIEM40GB_ndPmLu");
        headers.set("X-User-Authorization","Basic bG9naW46cGFzc3dvcmQ=");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }

    public ResponseEntity<PochtaCalculatorResponse> getTariffs(IndexPageDataDto deliveryParams) {
        final String REQUEST_URL = URL + "/1.0/tariff";

        HttpHeaders headers = getHttpHeaders();

        PochtaCalculatorRequest pochtaCalculatorTariffRequest = new PochtaCalculatorRequest();

        HttpEntity<PochtaCalculatorRequest> requestData = new HttpEntity<>(pochtaCalculatorTariffRequest, headers);
        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, PochtaCalculatorResponse.class);

    }
}
