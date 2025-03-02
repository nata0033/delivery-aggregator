package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.service.CdekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Data
public class AggregatorController {

    @Autowired
    private final CdekService cdekService;

    @GetMapping()
    public String findTarif(Model model) throws JsonProcessingException, JSONException {
        return "index";
    }

    @PostMapping()
    public ResponseEntity<String> index(@RequestParam("from_location") String fromLocation,
                                        @RequestParam("to_location") String toLocation,
                                        @RequestParam("weight") Integer weight) throws JsonProcessingException, JSONException {
        Integer fromLocationCode = cdekService.suggestCities(fromLocation);
        Integer toLocationCode = cdekService.suggestCities(toLocation);
        return cdekService.tariffList(fromLocationCode, toLocationCode, weight);
    }
}