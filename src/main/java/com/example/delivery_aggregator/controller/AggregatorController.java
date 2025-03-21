package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderRequest;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


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
        tariffsPageData.setService(new DeliveryService());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(Tariff::getPrice)).toList());
        model.addAttribute(tariffsPageData);
        return "tariffs";
    }

    @PostMapping("/order")
    public String getOrder(@Valid @ModelAttribute Tariff tariff, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, HttpServletRequest request, Model model){
//        orderPageData.getFromLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getFromLocation().getCity()));
//        orderPageData.getToLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getToLocation().getCity()));

        OrderPageData orderPageData = new OrderPageData();
        model.addAttribute(orderPageData);
        return "order";
    }

    @PostMapping("/create_order")
    public String order(@ModelAttribute OrderPageData orderPageData, HttpServletRequest request, HttpServletResponse response) {
        CdekOrderRequest cdekOrderRequest = new CdekOrderRequest();
        cdekOrderRequest.setType(2);
        cdekOrderRequest.getDeliveryRecipientCost().setValue((float) 0);
        return "account";
    }
}