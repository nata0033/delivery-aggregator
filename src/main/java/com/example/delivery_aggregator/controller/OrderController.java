package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.OrderDto;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dpd.ws.order2._2012_04_04.DpdOrderStatus2;

import java.util.*;

@Data
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CdekService cdekService;
    private final DpdService dpdService;

    private final OrderService orderService;

    private final AggregatorMapper aggregatorMapper;

    private static final String REDIRECT_URL = "redirect:/";

    @GetMapping
    public String orderPage(@CookieValue(name = "delivery_data", required = false) String deliveryData){
        if (deliveryData == null || deliveryData.isEmpty()) {
            return REDIRECT_URL;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(deliveryData);

            // Проверяем наличие tariff
            if (rootNode.path("tariff").isMissingNode() || rootNode.path("tariff").isNull()) {
                return REDIRECT_URL;
            }
            return "order";

        } catch (Exception e) {
            return REDIRECT_URL;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderPageDataDto orderPageData) {
        try {
            Order order;

            switch (orderPageData.getTariff().getService().getName()) {
                case "CDEK" -> {
                    ResponseEntity<CdekOrderResponseDto> cdekOrderResponseDto = cdekService.createOrder(orderPageData);
                    order = orderService.create(orderPageData, Objects.requireNonNull(cdekOrderResponseDto.getBody()));
                }
                case "DPD" -> {
                    ResponseEntity<List<DpdOrderStatus2>> dpdOrderResponseDto = dpdService.createOrder(orderPageData);
                    order = orderService.create(orderPageData, dpdOrderResponseDto.getBody().getFirst());
                }
                default ->
                        throw new IllegalStateException("Unexpected value: " + orderPageData.getTariff().getService().getName());
            }

            return ResponseEntity.ok(aggregatorMapper.orderToOrderDto(order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String uuid) {
        if (uuid == null || uuid.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            orderService.delete(uuid);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
