package com.example.delivery_aggregator.dto.aggregator;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class LocationDto {
    private String country;
    private String state;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String postalCode;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;
}
