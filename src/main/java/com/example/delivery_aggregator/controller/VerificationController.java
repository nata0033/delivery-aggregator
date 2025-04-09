package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.entity.VerificationCode;
import com.example.delivery_aggregator.repository.VerificationCodeRepository;
import com.example.delivery_aggregator.service.aggregator.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VerificationController {

    private final EmailService emailService;

    private final VerificationCodeRepository verificationCodeRepository;

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
            String code = generateRandomCode();
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setCode(code);
            verificationCodeRepository.save(verificationCode);

            emailService.sendVerificationCode(email, code);

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
            Boolean isValid = verificationCodeRepository.existsValidCode(email, code, LocalDateTime.now().minusDays(1));
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
}
