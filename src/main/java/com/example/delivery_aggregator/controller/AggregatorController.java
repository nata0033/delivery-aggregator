package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.service.db.DpdCityService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dpd.ws.geography._2015_05_20.City;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AggregatorController {

    private final DpdService dpdService;

    private  final DpdCityService dpdCityService;

    @Value("classpath:static/json/russian-cities.json")
    private Resource citiesResource;

    @GetMapping
    public String indexPage(){
        return "index";
    }

    @GetMapping("/cities/get")
    public ResponseEntity<String> getCities() throws IOException {
        InputStream inputStream = citiesResource.getInputStream();
        String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonContent);
    }

    @GetMapping("/cities/dpd/load")
    public ResponseEntity<List<City>> loadDpdCities() {
        List<City> dpdCityList = dpdService.getCitiesWithCashPay("RU");
        dpdCityService.saveAll(dpdCityList);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dpdCityList);
    }
}