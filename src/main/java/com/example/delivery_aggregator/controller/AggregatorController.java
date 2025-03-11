package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.FormDeliveryParams;
import com.example.delivery_aggregator.dto.cdek.TariffCodesResponse;
import com.example.delivery_aggregator.service.CdekService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Data
@RequiredArgsConstructor
public class AggregatorController {

    @Autowired
    private final CdekService cdekService;


    @GetMapping()
    public String index(){
        return "index";
    }

    @PostMapping()
    public ResponseEntity<TariffCodesResponse> getTariffs(@ModelAttribute FormDeliveryParams deliveryParams, Model model) {
        return cdekService.tariffList(deliveryParams);
    }

    @GetMapping("/test")
    public String test(Model model){
        return "test";
    }

}
