package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.pages.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.*;
import com.example.delivery_aggregator.service.api.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@Data
@RequiredArgsConstructor
public class AggregatorController {

    private final CdekService cdekService;

    private final PochtaService pochtaService;

    private final DellinService dellinService;

    private final PecomService pecomSevice;

    private final YandexService yandexService;
    
    private final UserService userService;

    private final ContactService contactService;

    private final CdekMapper cdekMapper;

    private final AggregatorMapper aggregatorMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/error")
    public String errorPage(){
        return "error";
    }

    @GetMapping()
    public String index(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute RegistrationPageDataDto registrationPageDto, Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        Contact contact = contactService.create(registrationPageDto);
        return "login";
    }

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageDataDto deliveryParams, Principal principal, Model model) {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        ResponseEntity<?> responseEntity = cdekService.getTariffs(deliveryParams);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponseDto) responseEntity.getBody(), deliveryParams);
        tariffsPageData.setService(new DeliveryServiceDto());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(TariffDto::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }

    @GetMapping("/order")
    public String orderPage(@CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal, Model model) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

//        orderPageData.getFromLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getFromLocation().getCity()));
//        orderPageData.getToLocation().setPostalCode(cdekService.getPostalCodes(orderPageData.getToLocation().getCity()));
        DeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryDataDto.class);

        OrderPageDataDto orderPageData = aggregatorMapper.deliveryDataToOrderPageData(deliveryData);
        orderPageData.setSender(new ContactDto());
        orderPageData.setRecipient(new ContactDto());
        model.addAttribute(orderPageData);
        return "order";
    }

    @PostMapping("/order")
    public String createOrder(@ModelAttribute OrderPageDataDto orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString) throws JsonProcessingException {
        DeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryDataDto.class);
        ResponseEntity<?> order = cdekService.createOrder(orderPageData, deliveryData);
        return "account";
    }

    @GetMapping("/account")
    public String accountPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "account";
    }
}
//9c91a5a9-a71f-4882-b963-10a9dbeccd51