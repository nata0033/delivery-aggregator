package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.aggregator.CookieDeliveryDataDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
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

@Data
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CdekService cdekService;
    private final DpdService dpdService;
    private final UserService userService;
    private final ContactService contactService;
    private final OrderService orderService;
    private final AggregatorMapper aggregatorMapper;
    private final ObjectMapper objectMapper;

    @GetMapping()
    public String orderPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "order";
    }

    @PostMapping("/create")
    public RedirectView createOrder(@RequestBody OrderPageDataDto orderPageData, @CookieValue(value = "delivery_data", required = false) String deliveryDataJsonString, Principal principal) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        User user = userService.getUserByLogin(userLogin);

        CookieDeliveryDataDto deliveryData = objectMapper.readValue(deliveryDataJsonString, CookieDeliveryDataDto.class);


        switch (orderPageData.getTariff().getService().getName()) {
            case "CDEK" -> { ResponseEntity<CdekOrderResponseDto> cdekOrderResponseDto = cdekService.createOrder(orderPageData); }
            case "DPD" -> { ResponseEntity<?> DpdOrderResponseDto = dpdService.createOrder(orderPageData); }
        }
 /*
        String serviceOrderNumber = cdekOrderResponseDto.getBody().getEntity().getUuid();
        String serviceName = "CDEK";
        orderService.create(orderPageData,serviceOrderNumber, serviceName, user, deliveryData);*/

        return new RedirectView("../account");
    }
}
