package com.example.delivery_aggregator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Configuration
@AllArgsConstructor
public class AppConfig {
    @Value("${authorization-codes.cdek.client-id}")
    public String cdekClientId;

    @Value("${authorization-codes.cdek.client-secret}")
    public String clientSecret;

    @Value("${authorization-codes.dpd.client-number}")
    public Long dpdClientNumber;

    @Value("${authorization-codes.dpd.client-key}")
    public String dpdClientKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){return builder.build();}
}
