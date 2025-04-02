package com.example.delivery_aggregator.dto.pages;

import com.example.delivery_aggregator.dto.db.AddressDto;
import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.db.OrderDto;
import lombok.Data;

import java.util.List;

@Data
public class AccountPageDataDto {
    private ContactDto user;
    private List<AddressDto> addresses;
    private List<OrderDto> orders;
    private List<ContactDto> contacts;
    private Integer orderQuantity;

}
