package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.pages.LocationDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@Data
@RequiredArgsConstructor
public class AggregatorController {

    private final VerificationController verificationController;

    @GetMapping("/test")
    public String test(){
        return "index";
    }

    @GetMapping()
    public String index(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "index";
    }

    @Value("classpath:static/json/russian-cities.json")
    private Resource citiesResource;

    @GetMapping("/getCities")
    public ResponseEntity<?> getCities() throws IOException {
        InputStream inputStream = citiesResource.getInputStream();
        String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonContent);
    }
}