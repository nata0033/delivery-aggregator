package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.entity.Payment;
import com.example.delivery_aggregator.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void create(Payment payment){
        paymentRepository.save(payment);
    }
}
