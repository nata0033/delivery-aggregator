package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.FormDeliveryParams;
import com.example.delivery_aggregator.dto.cdek.*;
import com.example.delivery_aggregator.mappers.CdekMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RequiredArgsConstructor
@Service
public class CdekService {

    private final String URL = "https://api.edu.cdek.ru";


    private final CdekMapper cdekMapper;

    @Autowired
    private RestTemplate restTemplate;

    public String getOAuthToken(){
        final String REQUEST_URL = URL + "/v2/oauth/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}";
        final String GRAND_TYPE = "client_credentials";
        final String CLIENT_ID = "wqGwiQx0gg8mLtiEKsUinjVSICCjtTEP";
        final String CLIENT_SECRET = "RmAmgvSgSl1yirlz9QupbzOJVqhCxcP5";

        OAuthTokenResponse response = restTemplate.postForEntity(REQUEST_URL, null, OAuthTokenResponse.class, GRAND_TYPE, CLIENT_ID, CLIENT_SECRET).getBody();
        return response.getAccessToken();
    }

    public  HttpHeaders getHttpHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        return headers;
    }

    public SuggestCityResponse getCitiesCode(String name) {
        final String REQUEST_URL = URL + "/v2/location/suggest/cities?name={name}&country_code=RU";
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = getHttpHeaders(ACCESS_TOKEN);

        ParameterizedTypeReference<List<SuggestCityResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SuggestCityResponse>> response = restTemplate.exchange(REQUEST_URL, HttpMethod.GET, new HttpEntity<>(headers), responseType, name);

        return response.getBody().getFirst();
    }


    public ResponseEntity<TariffCodesResponse> tariffList(FormDeliveryParams deliveryParams) {
        final String REQUEST_URL = URL + "/v2/calculator/tarifflist";
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = getHttpHeaders(ACCESS_TOKEN);

        SuggestCityResponse suggestFromCityResponse = getCitiesCode(deliveryParams.getFromLocation());
        SuggestCityResponse suggestToCityResponse = getCitiesCode(deliveryParams.getToLocation());

        Location fromLocation = cdekMapper.suggestCityResponseToLocation(suggestFromCityResponse);
        Location toLocation = cdekMapper.suggestCityResponseToLocation(suggestToCityResponse);

        CalculatorTariffRequest calculatorTariffRequest = new CalculatorTariffRequest();
        calculatorTariffRequest.setFromLocation(fromLocation);
        calculatorTariffRequest.setToLocation(toLocation);
        calculatorTariffRequest.setPackages(deliveryParams.getPackages());

        HttpEntity<CalculatorTariffRequest> requestData = new HttpEntity<>(calculatorTariffRequest, headers);
        ResponseEntity<TariffCodesResponse> tariffCodes = restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, TariffCodesResponse.class);
        return tariffCodes;
    }
}
