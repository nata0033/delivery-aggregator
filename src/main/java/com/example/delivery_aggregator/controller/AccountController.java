package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.ContactDto;
import com.example.delivery_aggregator.dto.aggregator.OrderDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AggregatorMapper aggregatorMapper;

    private final UserService userService;
    private final ContactService contactService;
    private final OrderService orderService;

    @GetMapping
    public String accountPage() {
        return "account";
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDto> getUserData(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            Contact contact = contactService.findByEmail(user.getLogin());

            if (contact == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(aggregatorMapper.contactToContactDto(contact));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDto>> getOrders(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            List<Order> orders = orderService.getOrders(user);

            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(aggregatorMapper.orderListToOrderDtoList(orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
