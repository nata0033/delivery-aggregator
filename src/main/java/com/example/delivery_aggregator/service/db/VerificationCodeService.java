package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.entity.VerificationCode;
import com.example.delivery_aggregator.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public VerificationCode create(String email){
        String code = generateRandomCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        return verificationCodeRepository.save(verificationCode);
    }

    public Boolean existsValidCode(String email, String code){
        return verificationCodeRepository.existsValidCode(email, code, LocalDateTime.now().minusDays(1));
    }
}
