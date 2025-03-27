package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

@Data
public class OrderPageDataDto {
 private ContactDto sender;
 private ContactDto recipient;
 private LocationDto fromLocation;
 private LocationDto toLocation;
 private TariffDto tariff;
 private CardDto card;
 private String comment;
}
