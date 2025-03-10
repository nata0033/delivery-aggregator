package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.entity.request.OAuthTokenInfo;
import com.example.delivery_aggregator.entity.request.SuggestCity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CdekService {

    private final String URL = "https://api.edu.cdek.ru";

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    public String getOAuthToken(){
        final String REQUEST_URL = URL + "/v2/oauth/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}";
        final String GRAND_TYPE = "client_credentials";
        final String CLIENT_ID = "wqGwiQx0gg8mLtiEKsUinjVSICCjtTEP";
        final String CLIENT_SECRET = "RmAmgvSgSl1yirlz9QupbzOJVqhCxcP5";

        OAuthTokenInfo response = restTemplate.postForEntity(REQUEST_URL, null, OAuthTokenInfo.class, GRAND_TYPE, CLIENT_ID, CLIENT_SECRET).getBody();
        return response.getAccess_token();
    }

    public  HttpHeaders getHttpHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        return headers;
    }

    public Integer getCitiesCode(String name) {
        final String REQUEST_URL = URL + "/v2/location/suggest/cities?name={name}&country_code=RU";
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = getHttpHeaders(ACCESS_TOKEN);

        ParameterizedTypeReference<List<SuggestCity>> ParamRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SuggestCity>> response = restTemplate.exchange(REQUEST_URL, HttpMethod.GET, new HttpEntity<>(headers), ParamRef, name);

        return response.getBody().getFirst().getCode();
    }

    public ResponseEntity<String> tariffList(Integer fromLocationCode, Integer toLocationCode,
                                             List<Integer> weight, List<Integer> length, List<Integer> width, List<Integer> height) {
        final String REQUEST_URL = URL + "/v2/calculator/tarifflist";
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = getHttpHeaders(ACCESS_TOKEN);

        Map<String, Object> fromLocation = new HashMap<>();
        fromLocation.put("code", fromLocationCode);

        Map<String, Object> toLocation = new HashMap<>();
        toLocation.put("code", toLocationCode);

        Map<String, Object> pack = new HashMap<>();
        pack.put("weight", weight);
        List<Map<String, Object>> packages= new ArrayList<>();
        packages.add(pack);

        Map<String, Object> body = new HashMap<>();
        body.put("from_location", fromLocation);
        body.put("to_location", toLocation);
        body.put("packages", packages);

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(REQUEST_URL, requestData, String.class);
    }
}
