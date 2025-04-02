package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.db.OrderDto;
import com.example.delivery_aggregator.dto.pages.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.DeliveryService;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.api.*;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.DeliveryServiceService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.*;

@Controller
@Data
@RequiredArgsConstructor
public class AggregatorController {

    private final CdekService cdekService;

    private final UserService userService;

    private final ContactService contactService;

    private final OrderService orderService;

    private final CdekMapper cdekMapper;

    private final AggregatorMapper aggregatorMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

//    @GetMapping("/error")
//    public String errorPage(){return "error";}

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

        contactService.create(registrationPageDto);
        return "login";
    }

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageDataDto indexPageDataDto, Principal principal, Model model) {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        ResponseEntity<?> responseEntity = cdekService.getTariffs(indexPageDataDto);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponseDto) responseEntity.getBody(), indexPageDataDto);
        tariffsPageData.setService(new DeliveryServiceDto());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(TariffDto::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }

    @PostMapping("/order")
    public String orderPage(@CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal, Model model) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        Contact contact = contactService.findByEmail(userLogin);
        DeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryDataDto.class);

        OrderPageDataDto orderPageData = aggregatorMapper.deliveryDataToOrderPageData(deliveryData, contact);
        orderPageData.setRecipient(new ContactDto());
        model.addAttribute("orderPageData", orderPageData);
        return "order";
    }

    @PostMapping("/order/create")
    public RedirectView createOrder(@ModelAttribute OrderPageDataDto orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        User user = userService.getUserByLogin(userLogin);

        DeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, DeliveryDataDto.class);
/*
Переделать так что бы работало для разных сервисов доставки
Из TariffsPageData.DeliveryServiceDto получить информацию о тарифе
Добавить в deliveryData поле с указанием сервиса
Получить сервис и с помощью условий определить конкретный сервис
Сделать запрос для него
*/
        ResponseEntity<CdekOrderResponseDto> cdekOrderResponseDto = cdekService.createOrder(orderPageData, deliveryData);

        String serviceOrderNumber = cdekOrderResponseDto.getBody().getEntity().getUuid();
        String serviceName = "CDEK";
        orderService.create(orderPageData,serviceOrderNumber, serviceName, user, deliveryData);

        return new RedirectView("../account");
    }

    private AccountPageDataDto createAccountPageDataDto(User user,Contact contact){
        AccountPageDataDto accountPageDataDto = aggregatorMapper.contactToAccountPageDataDto(user, contact);
        accountPageDataDto.setAddresses(new ArrayList<>());

        accountPageDataDto.setOrders(aggregatorMapper.OrdersToOrdersDto(orderService.getUserOrders(user)));
        return accountPageDataDto;
    }

    @GetMapping("/account")
    public String accountPage(Principal principal, Model model) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        User user = userService.getUserByLogin(userLogin);
        Contact contact = contactService.findByEmail(userLogin);

        model.addAttribute("accountPageData", createAccountPageDataDto(user, contact));
        model.addAttribute("ordersDataJsonString", objectMapper.writeValueAsString(createAccountPageDataDto(user, contact).getOrders()));
        return "account";
    }

    @PostMapping("/account/changeUser")
    public String changeContact(@ModelAttribute  ContactDto contactDto, Principal principal, Model model){
        return "account";
    }
}
//9c91a5a9-a71f-4882-b963-10a9dbeccd51