package com.example.delivery_aggregator.dto.pages;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phone;
    private String pic;
    private List<LocationDto> addresses;
}
