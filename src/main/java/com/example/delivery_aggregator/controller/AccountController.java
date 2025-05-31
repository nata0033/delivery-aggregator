package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Data
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final ContactService contactService;
    private final OrderService orderService;

    @GetMapping("/account")
    public String accountPage(Principal principal, Model model){
        model.addAttribute("isAuthenticated", true);
        return "account";
    }

    @GetMapping("/user")
    public ResponseEntity<Contact> getUserData(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            Contact contact = contactService.findByEmail(user.getLogin());

            if (contact == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(contact);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            List<Order> orders = orderService.getOrders(user);

            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
