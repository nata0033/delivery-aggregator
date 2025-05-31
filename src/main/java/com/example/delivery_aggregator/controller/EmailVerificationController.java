package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.entity.VerificationCode;
import com.example.delivery_aggregator.service.external_api.EmailService;
import com.example.delivery_aggregator.service.db.UserService;
import com.example.delivery_aggregator.service.db.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailService emailService;

    private final UserService userService;

    private final VerificationCodeService verificationCodeService;

    private static final String SUCCESS = "success";

    private static final String MESSAGE = "message";

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @PostMapping("/code/send")
    public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            VerificationCode verificationCode = verificationCodeService.create(email);
            emailService.sendVerificationCode(verificationCode.getEmail(), verificationCode.getCode());

            response.put(SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при отправке кода, попробуйте позже");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/code/confirm")
    public ResponseEntity<Map<String, Object>> confirmCode(@RequestParam String email, @RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean isValid = verificationCodeService.existsValidCode(email, code);
            response.put(SUCCESS, isValid);
            if (Boolean.FALSE.equals(isValid)) {
                response.put(MESSAGE, "Неверный код");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при проверке кода");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/check/unique")
    public ResponseEntity<Map<String, Object>> checkUserUnique(@RequestParam String email){
        Map<String, Object> response = new HashMap<>();
        try {
            boolean unique = true;
            User user = userService.findByLogin(email);
            if(user != null){
                unique = false;
            }
            response.put(SUCCESS, unique);
            if (Boolean.FALSE.equals(unique)) {
                response.put(MESSAGE, "Пользователь с такой почтой уже существует");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при проверке существования пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/check/exist")
    public ResponseEntity<Map<String, Object>> checkUserExist(@RequestParam String email){
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exist = true;
            User user = userService.findByLogin(email);
            if(user == null){
                exist = false;
            }
            response.put(SUCCESS, exist);
            if (Boolean.FALSE.equals(exist)) {
                response.put(MESSAGE, "Пользователя с такой почтой не существует ");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(SUCCESS, false);
            response.put(MESSAGE, "Ошибка при проверке существования пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
