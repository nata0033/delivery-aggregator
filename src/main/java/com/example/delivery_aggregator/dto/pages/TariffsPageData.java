package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

import java.util.List;

@Data
public class TariffsPageData {
    private LocationDto fromLocation;
    private LocationDto toLocation;
    private List<TariffDto> tariffs;
    private Integer packageQuantity;
    private Integer packageWeight;
}
