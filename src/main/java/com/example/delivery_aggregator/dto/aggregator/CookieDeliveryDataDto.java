package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class CookieDeliveryDataDto {
    private LocationDto fromLocation;
    private LocationDto toLocation;
    private List<PackageDto> packages;
    private ContactDto recipient;
    private TariffDto tariff;
}
