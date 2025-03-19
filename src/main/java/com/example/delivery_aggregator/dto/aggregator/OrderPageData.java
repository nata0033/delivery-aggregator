package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

@Data
public class OrderPageData {
 private User seller;
 private Location fromLocation;
 private Location toLocation;
 private Tariff tariff;
 private Card card;
}
