package com.example.delivery_aggregator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Controller
public class AggregatorController {

    @Value("classpath:static/json/russian-cities.json")
    private Resource citiesResource;

    @GetMapping()
    public String index(Principal principal, Model model){
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }

    @GetMapping("/getCities")
    public ResponseEntity<String> getCities() throws IOException {
        InputStream inputStream = citiesResource.getInputStream();
        String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonContent);
    }
}