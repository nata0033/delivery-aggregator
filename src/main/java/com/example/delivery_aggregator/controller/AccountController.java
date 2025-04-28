package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.aggregator.AccountPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@Data
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final ContactService contactService;
    private final OrderService orderService;
    private final AggregatorMapper aggregatorMapper;
    private final ObjectMapper objectMapper;
    private final VerificationController verificationController;


    @GetMapping("/account")
    public String accountPage(Principal principal, Model model){
        model.addAttribute("isAuthenticated", true);
        return "account";
    }

    @GetMapping("/account/accountPageData")
    @ResponseBody
    public ResponseEntity<?> getAccountPageData(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserByLogin(principal.getName());
            Contact contact = contactService.findByEmail(user.getLogin());
            AccountPageDataDto accountPageDataDto = aggregatorMapper.contactToAccountPageDataDto(user, contact);

            response.put("success", true);
            response.put("data", accountPageDataDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при получении данных пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/account/changeUserData")
    public ResponseEntity<?> changeUserData(@RequestPart("contactDto") ContactDto contactDto, @RequestPart(required = false) MultipartFile avatar, Principal principal) {
        contactService.update(contactDto, avatar);

        Map<String, Object> response = new HashMap<>();
        try {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/account/changeUserPassword")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestParam String oldPassword,
                                            @RequestParam String newPassword,
                                            Principal principal) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean success = userService.changePassword(principal.getName(), oldPassword, newPassword);
            response.put("success", success);
            if (!success) {
                response.put("message", "Неверный текущий пароль");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при изменении пароля");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
