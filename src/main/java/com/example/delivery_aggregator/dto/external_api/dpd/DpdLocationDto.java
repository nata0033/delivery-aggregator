package com.example.delivery_aggregator.dto.external_api.dpd;

import lombok.Data;

@Data
public class DpdLocationDto {
    private Long cityId;
    private String index;
    private String cityName;
    private Integer regionCode;
    private String countryCode;
}
