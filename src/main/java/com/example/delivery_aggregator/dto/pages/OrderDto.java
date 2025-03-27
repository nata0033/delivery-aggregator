package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

@Data
public class OrderDto {
    private String number;
    private String date;
    private String status;
    private Integer price;
}
