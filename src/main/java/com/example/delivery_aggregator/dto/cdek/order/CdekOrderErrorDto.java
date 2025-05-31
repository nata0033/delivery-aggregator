package com.example.delivery_aggregator.dto.cdek.order;

import lombok.Data;

@Data
public class CdekOrderErrorDto {
    private String code;
    private String message;
}