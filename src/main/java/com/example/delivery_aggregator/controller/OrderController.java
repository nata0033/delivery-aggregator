package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.service.db.OrderService;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dpd.ws.order2._2012_04_04.DpdOrderStatus2;

import java.util.List;

@Data
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CdekService cdekService;
    private final DpdService dpdService;

    private final OrderService orderService;

    @GetMapping()
    public String orderPage(){
        return "order";
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderPageDataDto orderPageData) {
        try {
            Order order = new Order();

            switch (orderPageData.getTariff().getService().getName()) {
                case "CDEK" -> {
                    ResponseEntity<CdekOrderResponseDto> cdekOrderResponseDto = cdekService.createOrder(orderPageData);
                    order = orderService.create(orderPageData, cdekOrderResponseDto.getBody());
                }
                case "DPD" -> {
                    ResponseEntity<List<DpdOrderStatus2>> dpdOrderResponseDto = dpdService.createOrder(orderPageData);
                    order = orderService.create(orderPageData, dpdOrderResponseDto.getBody());
                }
            }

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Order());
        }
    }
}
