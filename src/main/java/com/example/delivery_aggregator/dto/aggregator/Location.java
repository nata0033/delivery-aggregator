package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"date", "country", "postalCode"})
public class Location {
    private String country;
    private String state;
    private String city;
    private String street;
    private String apartment;
    private String postalCode;
    private String date;
}
