package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class AccountPageDataDto {
    private ContactDto userData;
    private List<AddressDto> addresses;
    private List<OrderDto> sendOrders;
    private List<OrderDto> receivedOrders;
    private List<ContactDto> contacts;
}
