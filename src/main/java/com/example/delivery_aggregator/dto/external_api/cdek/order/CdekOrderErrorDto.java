package com.example.delivery_aggregator.dto.external_api.cdek.order;

import lombok.Data;

@Data
public class CdekOrderErrorDto {
    private String code;
    private String message;
}