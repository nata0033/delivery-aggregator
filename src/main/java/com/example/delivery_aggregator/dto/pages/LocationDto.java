package com.example.delivery_aggregator.dto.pages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"date", "country", "postalCode"})
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
