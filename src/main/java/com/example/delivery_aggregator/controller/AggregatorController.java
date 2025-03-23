package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private final AggregatorMapper aggregatorMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping()
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/account")
    public String accountPage(){
        return "account";
    }

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageData deliveryParams, Model model) {
        ResponseEntity<?> responseEntity = cdekService.getTariffs(deliveryParams);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponse) responseEntity.getBody(), deliveryParams);
        tariffsPageData.setService(new DeliveryService());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(Tariff::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }

    @GetMapping("/order")
    public String orderPage(@CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Model model) throws JsonProcessingException {
//        orderPageData.getFromLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getFromLocation().getCity()));
//        orderPageData.getToLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getToLocation().getCity()));
        DeliveryData deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryData.class);

        OrderPageData orderPageData = aggregatorMapper.deliveryDataToOrderPageData(deliveryData);
        orderPageData.setSender(new User());
        orderPageData.setRecipient(new User());
        model.addAttribute(orderPageData);
        return "order";
    }

    @PostMapping("/order")
    //public String createOrder(@ModelAttribute OrderPageData orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString) throws JsonProcessingException {
    public ResponseEntity<?> createOrder (@ModelAttribute OrderPageData orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString) throws JsonProcessingException {
        DeliveryData deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryData.class);
        ResponseEntity<?> response = cdekService.createOrder(orderPageData, deliveryData);
        //return "account";
        return response;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginPageData loginPageData){
        return "account";
    }
}
//9c91a5a9-a71f-4882-b963-10a9dbeccd51