package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.IndexPageData;
import com.example.delivery_aggregator.dto.aggregator.OrderPageData;
import com.example.delivery_aggregator.dto.aggregator.Tariff;
import com.example.delivery_aggregator.dto.aggregator.TariffsPageData;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;


@Controller
@Data
@RequiredArgsConstructor
public class AggregatorController {

    private final CdekService cdekService;

    private final PochtaService pochtaService;

    private final DellinService dellinServiсe;

    private final PecomService pecomSevice;

    private final YandexService yandexService;

    private final CdekMapper cdekMapper;

    @GetMapping()
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/account")
    public String account(){
        return "account";
    }

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageData deliveryParams, Model model) {
        ResponseEntity<?> responseEntity = cdekService.getTariffs(deliveryParams);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponse) responseEntity.getBody(), deliveryParams);
        tariffsPageData.setServiceName("Cdek");
        tariffsPageData.setServiceLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().sorted(Comparator.comparing(Tariff::getPrice)).toList());
        model.addAttribute(tariffsPageData);

        return "tariffs";
    }

    @PostMapping("/order")
    public String getOrder(@ModelAttribute OrderPageData orderPageData, Model model){
//        orderPageData.getFromLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getFromLocation().getCity()));
//        orderPageData.getToLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getToLocation().getCity()));

        model.addAttribute(orderPageData);
        return "order";
    }

    @PostMapping("/create_order")
    public String order(@ModelAttribute OrderPageData orderPageData){
        return "account.html";
    }
}