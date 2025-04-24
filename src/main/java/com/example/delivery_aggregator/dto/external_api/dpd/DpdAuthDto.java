package com.example.delivery_aggregator.dto.external_api.dpd;

import lombok.Data;

@Data
public class DpdAuthDto {
    private Long clientNumber;
    private String clientKey;
}
