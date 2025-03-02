package com.example.delivery_aggregator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CdekService {

    private final String URL = "https://api.edu.cdek.ru";

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    public String getOAuthToken() throws JsonProcessingException {
        final String REQUEST_URL = URL + "/v2/oauth/token?grant_type={grantType}&client_id={clientId}&client_secret={clientSecret}";
        final String GRAND_TYPE = "client_credentials";
        final String CLIENT_ID = "wqGwiQx0gg8mLtiEKsUinjVSICCjtTEP";
        final String CLIENT_SECRET = "RmAmgvSgSl1yirlz9QupbzOJVqhCxcP5";

        ResponseEntity<?> response = restTemplate.postForEntity(REQUEST_URL, null, String.class, GRAND_TYPE, CLIENT_ID, CLIENT_SECRET);
        HashMap responseMap = new ObjectMapper().readValue(response.getBody().toString(), HashMap.class);

        return responseMap.get("access_token").toString();
    }

    public Integer suggestCities(String name) throws JsonProcessingException, JSONException {
        final String REQUEST_URL = URL + "/v2/location/suggest/cities?name={name}";
        final String ACCESS_TOKEN = getOAuthToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(ACCESS_TOKEN);

       ResponseEntity<ArrayList> response = restTemplate.exchange(REQUEST_URL, HttpMethod.GET, new HttpEntity<>(headers), ArrayList.class, name);
       ArrayList<HashMap<String, Object>> jsonResponseMap = response.getBody();

       return (Integer) jsonResponseMap.get(0).get("code");
    }

    public ResponseEntity<String> tariffList(Integer fromLocationCode, Integer toLocationCode,
                                             Integer weight) throws JsonProcessingException {
        final String REQUEST_URL = URL + "/v2/calculator/tarifflist";
        final String ACCESS_TOKEN = getOAuthToken();

        Map<String, Object> fromLocation = new HashMap<>();
        fromLocation.put("code", fromLocationCode);

        Map<String, Object> toLocation = new HashMap<>();
        toLocation.put("code", toLocationCode);

        Map<String, Object> pack = new HashMap<>();
        pack.put("weight", weight);
        ArrayList<Map<String, Object>> packages= new ArrayList<>();
        packages.add(pack);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(ACCESS_TOKEN);

        Map<String, Object> body = new HashMap<>();
        body.put("from_location", fromLocation);
        body.put("to_location", toLocation);
        body.put("packages", packages);

        HttpEntity<Map<String, Object>> requestData = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(REQUEST_URL, requestData, String.class);
    }
}
