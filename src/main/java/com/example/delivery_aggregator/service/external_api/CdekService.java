package com.example.delivery_aggregator.service.external_api;

import com.example.delivery_aggregator.dto.cdek.CdekOAuthTokenResponseDto;
import com.example.delivery_aggregator.dto.cdek.CdekSuggestCityResponseDto;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderRequestDto;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.aggregator.IndexPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorRequestDto;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.mappers.CdekMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@AllArgsConstructor
@Service
public class CdekService {

    private static final String URL = "https://api.edu.cdek.ru";

    @Value("${authorization-codes.cdek.client-id}")
    private String cdekClientId;

    @Value("${authorization-codes.cdek.client-secret}")
    private String clientSecret;

    private final CdekMapper cdekMapper;

    private final RestTemplate restTemplate;

    public String getOAuthToken(){
        final String REQUEST_URL = URL + "/v2/oauth/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}";
        final String GRAND_TYPE = "client_credentials";

        CdekOAuthTokenResponseDto response = restTemplate.postForEntity(REQUEST_URL, null, CdekOAuthTokenResponseDto.class, GRAND_TYPE, cdekClientId, clientSecret).getBody();
        return response.getAccessToken();
    }

    public  HttpHeaders getHttpHeaders(){
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(ACCESS_TOKEN);
        return headers;
    }

    public CdekSuggestCityResponseDto getCitiesCode(String name) {
        final String REQUEST_URL = URL + "/v2/location/suggest/cities?name={name}&country_code=RU";

        HttpHeaders headers = getHttpHeaders();

        ParameterizedTypeReference<List<CdekSuggestCityResponseDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<CdekSuggestCityResponseDto>> response = restTemplate.exchange(REQUEST_URL, HttpMethod.GET, new HttpEntity<>(headers), responseType, name);

        return response.getBody().getFirst();
    }

    public ResponseEntity<CdekCalculatorResponseDto> getTariffs(IndexPageDataDto indexPageDataDto) {
        final String REQUEST_URL = URL + "/v2/calculator/tarifflist";

        HttpHeaders headers = getHttpHeaders();

        CdekSuggestCityResponseDto suggestFromCityResponse = getCitiesCode(indexPageDataDto.getFromLocation().getCity());
        CdekSuggestCityResponseDto suggestToCityResponse = getCitiesCode(indexPageDataDto.getToLocation().getCity());

        CdekCalculatorRequestDto cdekCalculatorTariffRequest = cdekMapper.indexPageDataDtoAndCdekSuggestCityResponseDtoToCdekCalculatorRequestDto(
                indexPageDataDto, suggestFromCityResponse, suggestToCityResponse);

        HttpEntity<CdekCalculatorRequestDto> requestData = new HttpEntity<>(cdekCalculatorTariffRequest, headers);
        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, CdekCalculatorResponseDto.class);
    }

    public ResponseEntity<CdekOrderResponseDto> createOrder(OrderPageDataDto orderPageData){
        final String REQUEST_URL = URL + "/v2/orders";

        HttpHeaders headers = getHttpHeaders();

        CdekOrderRequestDto cdekOrderRequest = cdekMapper.OrderPageDataDtoToCdekOrderRequest(orderPageData);

        HttpEntity<CdekOrderRequestDto> requestData = new HttpEntity<>(cdekOrderRequest, headers);
        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, CdekOrderResponseDto.class);
    }

}
