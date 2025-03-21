package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;

import java.util.List;

@Data
public class OrderPageData {
 private User sender;
 private User recipient;
 private Location fromLocation;
 private Location toLocation;
 private Tariff tariff;
 private Card card;
 private List<Package> packages;
}
