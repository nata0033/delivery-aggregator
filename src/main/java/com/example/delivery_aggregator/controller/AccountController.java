package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.pages.AccountPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.db.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
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
    public String accountPage(Principal principal, Model model) throws JsonProcessingException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        User user = userService.getUserByLogin(userLogin);
        Contact contact = contactService.findByEmail(userLogin);

        AccountPageDataDto accountPageData = createAccountPageDataDto(user, contact);
        model.addAttribute("accountPageData", accountPageData);
        model.addAttribute("ordersDataJsonString", objectMapper.writeValueAsString(accountPageData.getOrders()));
        model.addAttribute("addressesDataJsonString", objectMapper.writeValueAsString(accountPageData.getAddresses()));
        model.addAttribute("contactsDataJsonString", objectMapper.writeValueAsString(accountPageData.getContacts()));
        return "account";
    }

    private AccountPageDataDto createAccountPageDataDto(User user,Contact contact){
        AccountPageDataDto accountPageDataDto = aggregatorMapper.contactToAccountPageDataDto(user, contact);
        accountPageDataDto.setAddresses(new ArrayList<>());

        accountPageDataDto.setOrders(aggregatorMapper.OrdersToOrdersDto(orderService.getUserOrders(user)));
        return accountPageDataDto;
    }

    @PostMapping("/account/changeUser")
    public String changeUserData(@ModelAttribute ContactDto contactDto,
                                 @RequestParam(required = false) MultipartFile avatar,
                                 Principal principal,
                                 Model model) throws IOException, JsonProcessingException {

        String userLogin = principal.getName();
        User user = userService.getUserByLogin(userLogin);

        // Обновляем данные пользователя
        userService.updateUser(contactDto, avatar);

        // Если изменился email, отправляем код подтверждения
        if (!user.getLogin().equals(contactDto.getEmail())) {
            verificationController.sendCode(contactDto.getEmail());
        }

        // Обновляем данные для отображения
        Contact contact = contactService.findByEmail(user.getLogin());
        AccountPageDataDto accountPageData = createAccountPageDataDto(user, contact);

        model.addAttribute("accountPageData", accountPageData);
        model.addAttribute("ordersDataJsonString", objectMapper.writeValueAsString(accountPageData.getOrders()));
        model.addAttribute("addressesDataJsonString", objectMapper.writeValueAsString(accountPageData.getAddresses()));
        model.addAttribute("contactsDataJsonString", objectMapper.writeValueAsString(accountPageData.getContacts()));

        return "account";
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
