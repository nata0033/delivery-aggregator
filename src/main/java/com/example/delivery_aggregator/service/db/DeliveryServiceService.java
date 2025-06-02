package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.entity.DeliveryService;
import com.example.delivery_aggregator.repository.DeliveryServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceService {

    private final DeliveryServiceRepository deliveryServiceRepository;

    public DeliveryService findByName(String name) {
        return deliveryServiceRepository.findByName(name).orElseGet(() -> createWithName(name));
    }

    public DeliveryService createWithName(String name){
        DeliveryService newDeliveryService = new DeliveryService();
        newDeliveryService.setName(name);
        return deliveryServiceRepository.save(newDeliveryService);
    }
}
