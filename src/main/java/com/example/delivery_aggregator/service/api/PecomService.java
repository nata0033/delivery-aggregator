package com.example.delivery_aggregator.service.api;

import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pecom.PecomCalculatorRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PecomService {

    private final String URL = "https://pecom.ru";

    private final RestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    public HashMap<String, Map<String, String>> getTowns() throws JsonProcessingException {
        final String REQUEST_URL = URL + "/ru/calc/towns.php";

        String townsString = restTemplate.getForObject(REQUEST_URL, String.class);

        TypeReference<HashMap<String, Map<String, String>>> typeRef = new TypeReference<HashMap<String, Map<String, String>>>() {};
        return mapper.readValue(townsString, typeRef);
    }

    //public ResponseEntity<PecomCalculatorTariffResponse> getTariffs(FormDeliveryParams deliveryParams) {
    public ResponseEntity<String> getTariffs(IndexPageDataDto deliveryParams) {
        final String REQUEST_URL = URL + "/bitrix/components/pecom/calc/ajax.php";

        PecomCalculatorRequest pecomCalculatorTariffRequest = new PecomCalculatorRequest();

        String params = "?places[0][0]=1&places[0][1]=2&places[0][2]=3&places[0][3]=4&places[0][4]=5&places[0][5]=1&places[0][6]=1&take[town]=-457&take[tent]=1&take[gidro]=1&take[manip]=1&take[speed]=1&take[moscow]=1&deliver[town]=64883&deliver[tent]=1&deliver[gidro]=1&deliver[manip]=1&deliver[speed]=0&deliver[moscow]=0&plombir=12&strah=33&ashan=1&night=1&pal=1&pallets=4";
        //return restTemplate.getForEntity(REQUEST_URL + params, PecomCalculatorTariffResponse.class);
        //return restTemplate.getForEntity(REQUEST_URL + params, String.class);
        //return restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(pecomCalculatorTariffRequest), PecomCalculatorTariffResponse.class);
        return restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(pecomCalculatorTariffRequest), String.class);
    }
}
