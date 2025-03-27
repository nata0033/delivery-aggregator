package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

import java.util.List;

@Data
public class AccountPageDto {
    private UserDto user;
    private List<OrderDto> orders;
    private List<ContactDto> contacts;

}
