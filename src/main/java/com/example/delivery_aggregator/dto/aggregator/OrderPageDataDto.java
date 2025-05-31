package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class OrderPageDataDto {
 private ContactDto sender;
 private ContactDto recipient;
 private LocationDto fromLocation;
 private LocationDto toLocation;
 private TariffDto tariff;
 private String comment;
 private List<PackageDto> packages;
}
