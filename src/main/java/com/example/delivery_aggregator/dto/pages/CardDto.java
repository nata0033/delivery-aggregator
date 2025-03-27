package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

@Data
public class CardDto {
    private String number;
    private String name;
    private String date;
    private String expiration;
}
