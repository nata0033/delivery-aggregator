package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"shipmentDate", "country", "postalCode", "fullName"})
public class Location {
    private String country;
    private String state;
    private String city;
    private String street;
    private String apartment;
    private String fullName;
    private String postalCode;
    private String shipmentDate;
}
