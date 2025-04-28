package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

@Data
public class CardDto {
    private String number;
    private String name;
    private String date;
    private String expiration;
}
