package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.cdek.info.CdekInfoResponseDto;
import com.example.delivery_aggregator.entity.*;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.OrderRepository;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dpd.ws.order2._2012_04_04.DpdOrderStatus2;
import ru.dpd.ws.tracing._2011_11_18.StateParcels;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ContactService contactService;

    private final UserService userService;

    private final DeliveryServiceService deliveryServiceService;

    private final PackageService packageService;

    private final CdekService cdekService;
    private final DpdService dpdService;

    private final AggregatorMapper aggregatorMapper;

    public <T> Order create(OrderPageDataDto orderPageDataDto, T responseDto) {
        User user = userService.findByLogin(orderPageDataDto.getSender().getEmail());
        Contact contact = contactService.findUserContact(orderPageDataDto.getRecipient(), user);

        String deliveryServiceName;
        Order order;

        switch (responseDto) {
            case CdekOrderResponseDto cdekResponse -> {
                deliveryServiceName = "CDEK";
                DeliveryService deliveryService = deliveryServiceService.findByName(deliveryServiceName);
                order = aggregatorMapper.orderPageDataDtoAndCdekOrderResponseDto(orderPageDataDto, user, contact, deliveryService, cdekResponse);
            }
            case DpdOrderStatus2 dpdResponse -> {
                deliveryServiceName = "DPD";
                DeliveryService deliveryService = deliveryServiceService.findByName(deliveryServiceName);
                order = aggregatorMapper.orderPageDataDtoAndCdekOrderResponseDto(orderPageDataDto, user, contact, deliveryService, dpdResponse);
            }
            default -> throw new IllegalStateException("Unexpected value: " + responseDto);
        }
        order = orderRepository.save(order);
        packageService.saveList(order, orderPageDataDto.getPackages());
        return order;
    }

    public List<Order> getOrders(User user){
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public List<Order> getOrdersWithUpdate(User user){
        List<Order> orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user);
        return orders.stream().map(this::update).toList();
    }

    private static final List<String> FINAL_STATUSES = List.of("DELIVERED", "CANCELED");

    public Order update(Order order) {
        if (order.getDeliveryService() == null || order.getServiceOrderNumber() == null) {
            return order;
        }

        if (FINAL_STATUSES.stream().noneMatch(status -> status.equals(order.getStatus()))  ||
                        order.getLastUpdate().isAfter(LocalDateTime.now().minusDays(1))) {
            return order;
        }

        if ("CDEK".equals(order.getDeliveryService().getName())) {
            ResponseEntity<CdekInfoResponseDto> response = cdekService.getOrderInfoByUUID(order.getServiceOrderNumber());
            if (response.getBody() != null &&
                    response.getBody().getRequests() != null &&
                    !response.getBody().getRequests().isEmpty()) {

                String newStatus = response.getBody().getRequests().getFirst().getState();
                order.setStatus(aggregatorMapper.getOrderStatus(newStatus));
            }
        } else if ("DPD".equals(order.getDeliveryService().getName())) {
            ResponseEntity<StateParcels> response = dpdService.getOrderInfoByClientOrderNr(order.getServiceOrderNumber());
            if (response.getBody() != null &&
                    response.getBody().getStates() != null &&
                    !response.getBody().getStates().isEmpty()) {

                String newStatus = response.getBody().getStates().getLast().getNewState();
                order.setStatus(aggregatorMapper.getOrderStatus(newStatus));
            }
        }

        return orderRepository.save(order);
    }

    public void delete(String uuid) {
        Order order = orderRepository.getReferenceById(UUID.fromString(uuid));

        if(order.getDeliveryService().getName().equals("CDEK")){
            cdekService.deleteOrderByUUID(order.getServiceOrderNumber());
        } else if(order.getDeliveryService().getName().equals("DPD")){
            dpdService.deleteOrderByOrderNumberInternal(order.getServiceOrderNumber());
        }

        order.setStatus("CANCELED");
        orderRepository.save(order);
    }
}
