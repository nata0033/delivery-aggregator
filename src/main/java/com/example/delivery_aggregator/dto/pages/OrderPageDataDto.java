package com.example.delivery_aggregator.dto.pages;

import com.example.delivery_aggregator.dto.db.ContactDto;
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
