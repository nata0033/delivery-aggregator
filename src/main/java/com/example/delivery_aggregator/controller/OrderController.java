package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.aggregator.CookieDeliveryDataDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@Data
@RequiredArgsConstructor
public class OrderController {

    private final CdekService cdekService;
    private final UserService userService;
    private final ContactService contactService;
    private final OrderService orderService;
    private final AggregatorMapper aggregatorMapper;
    private final ObjectMapper objectMapper;

    @PostMapping("/order")
    public String orderPage(@CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal, Model model) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        Contact contact = contactService.findByEmail(userLogin);
        CookieDeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, CookieDeliveryDataDto.class);

        OrderPageDataDto orderPageData = aggregatorMapper.deliveryDataToOrderPageData(deliveryData, contact);
        orderPageData.setRecipient(new ContactDto());
        model.addAttribute("orderPageData", orderPageData);
        return "order";
    }

    @PostMapping("/order/create")
    public RedirectView createOrder(@ModelAttribute OrderPageDataDto orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        User user = userService.getUserByLogin(userLogin);

        CookieDeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, CookieDeliveryDataDto.class);

        ResponseEntity<CdekOrderResponseDto> cdekOrderResponseDto = cdekService.createOrder(orderPageData, deliveryData);

        String serviceOrderNumber = cdekOrderResponseDto.getBody().getEntity().getUuid();
        String serviceName = "CDEK";
        orderService.create(orderPageData,serviceOrderNumber, serviceName, user, deliveryData);

        return new RedirectView("../account");
    }
}
