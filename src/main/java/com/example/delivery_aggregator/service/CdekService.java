package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.aggregator.DeliveryData;
import com.example.delivery_aggregator.dto.aggregator.IndexPageData;
import com.example.delivery_aggregator.dto.aggregator.OrderPageData;
import com.example.delivery_aggregator.dto.cdek.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorRequest;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderRequest;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponse;
import com.example.delivery_aggregator.dto.cdek.order.DeliveryRecipientCost;
import com.example.delivery_aggregator.dto.cdek.order.Package;
import com.example.delivery_aggregator.mappers.CdekMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@Service
public class CdekService {

    private final String URL = "https://api.edu.cdek.ru";

    private final CdekMapper cdekMapper;

    private final RestTemplate restTemplate;

    public String getOAuthToken(){
        final String REQUEST_URL = URL + "/v2/oauth/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}";
        final String GRAND_TYPE = "client_credentials";

        final String CLIENT_ID = "wqGwiQx0gg8mLtiEKsUinjVSICCjtTEP";
        final String CLIENT_SECRET = "RmAmgvSgSl1yirlz9QupbzOJVqhCxcP5";

        OAuthTokenResponse response = restTemplate.postForEntity(REQUEST_URL, null, OAuthTokenResponse.class, GRAND_TYPE, CLIENT_ID, CLIENT_SECRET).getBody();
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

    public SuggestCityResponse getCitiesCode(String name) {
        final String REQUEST_URL = URL + "/v2/location/suggest/cities?name={name}&country_code=RU";

        HttpHeaders headers = getHttpHeaders();

        ParameterizedTypeReference<List<SuggestCityResponse>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SuggestCityResponse>> response = restTemplate.exchange(REQUEST_URL, HttpMethod.GET, new HttpEntity<>(headers), responseType, name);

        return response.getBody().getFirst();
    }

    public ResponseEntity<CdekCalculatorResponse> getTariffs(IndexPageData deliveryParams) {
        final String REQUEST_URL = URL + "/v2/calculator/tarifflist";

        HttpHeaders headers = getHttpHeaders();

        SuggestCityResponse suggestFromCityResponse = getCitiesCode(deliveryParams.getFromLocation().getCity());
        SuggestCityResponse suggestToCityResponse = getCitiesCode(deliveryParams.getToLocation().getCity());

        CdekCalculatorRequest cdekCalculatorTariffRequest = new CdekCalculatorRequest();
        cdekCalculatorTariffRequest.setFromLocation(cdekMapper.suggestCityResponseToLocation(suggestFromCityResponse));
        cdekCalculatorTariffRequest.setToLocation(cdekMapper.suggestCityResponseToLocation(suggestToCityResponse));
        cdekCalculatorTariffRequest.setPackages(deliveryParams.getPackages().stream().map(cdekMapper::aggregatorPackageToCalculatorCdekPackage).toList());

        HttpEntity<CdekCalculatorRequest> requestData = new HttpEntity<>(cdekCalculatorTariffRequest, headers);
        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, CdekCalculatorResponse.class);
    }

    public String getPostalCodes(String name){
        final String REQUEST_URL = URL + "/v2/location/postcodes?code={code}";

        Integer code = this.getCitiesCode(name).getCode();

        HttpHeaders headers = getHttpHeaders();

        ResponseEntity<CdekPostcodesResponse> response = restTemplate.exchange(REQUEST_URL, HttpMethod.POST, new HttpEntity<>(headers), CdekPostcodesResponse.class, code);
        return response.getBody().getPostalCodes().getFirst();
    }

    public ResponseEntity<CdekOrderResponse> createOrder(OrderPageData orderPageData, DeliveryData deliveryData){
        final String REQUEST_URL = URL + "/v2/orders";

        HttpHeaders headers = getHttpHeaders();

        CdekOrderRequest cdekOrderRequest = cdekMapper.orderPageDataAndDeliveryDataToCdekOrderRequest(orderPageData,deliveryData);
        cdekOrderRequest.setType(2);
        cdekOrderRequest.setDeliveryRecipientCost(new DeliveryRecipientCost());
        cdekOrderRequest.getDeliveryRecipientCost().setValue((float) 0);
        cdekOrderRequest.getSender().setCompany("Агрегатор");
        HttpEntity<CdekOrderRequest> requestData = new HttpEntity<>(cdekOrderRequest, headers);
        cdekOrderRequest.setPackages(
                IntStream.range(0, deliveryData.getPackages().size())
                        .mapToObj(i -> {
                            Package p = cdekMapper.aggregatorPackageToOrderCdekPackage(deliveryData.getPackages().get(i), orderPageData);
                            p.setNumber(String.valueOf(i));
                            return p;
                        }).toList()
        );

        return restTemplate.exchange(REQUEST_URL, HttpMethod.POST, requestData, CdekOrderResponse.class);
    }
}
