package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.pages.DeliveryDataDto;
import com.example.delivery_aggregator.dto.pages.OrderPageDataDto;
import com.example.delivery_aggregator.entity.*;
import com.example.delivery_aggregator.entity.Package;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ContactService contactService;

    private final PaymentService paymentService;

    private final PackageService packageService;

    private final DeliveryServiceService deliveryServiceService;

    private final AggregatorMapper aggregatorMapper;

    public void create(OrderPageDataDto orderPageDataDto, String serviceOrderNumber, String serviceName, User user, DeliveryDataDto deliveryData){
        Contact contact = contactService.create(orderPageDataDto, user);

        DeliveryService deliveryService = deliveryServiceService.createWithName(serviceName);

        Order order = aggregatorMapper.orderPageDataDtoToOrder(orderPageDataDto, serviceOrderNumber);
        order.setServiceOrderNumber(serviceOrderNumber);
        order.setUser(user);
        order.setContact(contact);
        order.setDeliveryService(deliveryService);
        order = orderRepository.save(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        paymentService.create(payment);

        List<Package> packages = aggregatorMapper.packagesDtoToPackages(deliveryData.getPackages(), order);
        packageService.createList(packages);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user);
    }
}
