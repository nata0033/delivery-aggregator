package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryDataDto {
    private LocationDto fromLocation;
    private LocationDto toLocation;
    private List<PackageDto> packages;
    private TariffDto tariff;
}
