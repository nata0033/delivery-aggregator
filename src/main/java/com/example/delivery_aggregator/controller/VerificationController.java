package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.entity.VerificationCode;
import com.example.delivery_aggregator.service.aggregator.EmailService;
import com.example.delivery_aggregator.service.db.UserService;
import com.example.delivery_aggregator.service.db.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VerificationController {

    private final EmailService emailService;

    private final UserService userService;

    private final VerificationCodeService verificationCodeService;

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            VerificationCode verificationCode = verificationCodeService.create(email);
            emailService.sendVerificationCode(verificationCode.getEmail(), verificationCode.getCode());

            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при отправке кода, попробуйте позже");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/confirm-code")
    public ResponseEntity<Map<String, Object>> confirmCode(@RequestParam String email, @RequestParam String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean isValid = verificationCodeService.existsValidCode(email, code);
            response.put("success", isValid);
            if (!isValid) {
                response.put("message", "Неверный код");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при проверке кода");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/check-email-unique")
    public ResponseEntity<?> checkUserUnique(@RequestParam String email){
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean unique = true;
            User user = userService.getUserByLogin(email);
            if(user != null){
                unique = false;
            }
            response.put("success", unique);
            if (!unique) {
                response.put("message", "Пользователь с такой почтой уже существует");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при проверке существования пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/check-email-exist")
    public ResponseEntity<?> checkUserExist(@RequestParam String email){
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean exist = true;
            User user = userService.getUserByLogin(email);
            if(user == null){
                exist = false;
            }
            response.put("success", exist);
            if (!exist) {
                response.put("message", "Пользователя с такой почтой не существует ");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при проверке существования пользователя");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
