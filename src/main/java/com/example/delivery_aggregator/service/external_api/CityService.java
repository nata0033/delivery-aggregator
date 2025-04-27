package com.example.delivery_aggregator.service.external_api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CityService {
    private final String  URL = "https://nominatim.openstreetmap.org/search?<params>";

    private final RestTemplate restTemplate;
}
