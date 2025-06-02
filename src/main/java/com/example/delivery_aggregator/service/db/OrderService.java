package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.entity.*;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dpd.ws.order2._2012_04_04.DpdOrderStatus2;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ContactService contactService;

    private final UserService userService;

    private final DeliveryServiceService deliveryServiceService;

    private final PackageService packageService;

    private final AggregatorMapper aggregatorMapper;

    public <T> Order create(OrderPageDataDto orderPageDataDto, T responseDto) {
        User user = userService.findByLogin(orderPageDataDto.getSender().getEmail());
        Contact contact = contactService.findUserContact(orderPageDataDto.getRecipient(), user);

        String deliveryServiceName;
        Order order;

        if (responseDto instanceof CdekOrderResponseDto cdekResponse) {
            deliveryServiceName = "CDEK";
            DeliveryService deliveryService = deliveryServiceService.findByName(deliveryServiceName);
            order = aggregatorMapper.orderPageDataDtoAndCdekOrderResponseDto(orderPageDataDto, user, contact, deliveryService, cdekResponse);
        } else if (responseDto instanceof DpdOrderStatus2 dpdResponse) {
            deliveryServiceName = "DPD";
            DeliveryService deliveryService = deliveryServiceService.findByName(deliveryServiceName);
            order = aggregatorMapper.orderPageDataDtoAndCdekOrderResponseDto(orderPageDataDto, user, contact, deliveryService, dpdResponse);
        } else {
            throw new IllegalArgumentException("Unsupported response DTO type: " + responseDto.getClass());
        }
        order = orderRepository.save(order);
        packageService.saveList(order, orderPageDataDto.getPackages());
        return order;
    }

    public List<Order> getOrders(User user){
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

}
