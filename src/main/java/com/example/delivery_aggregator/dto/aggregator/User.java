package com.example.delivery_aggregator.dto.aggregator;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"email", "phone"})
public class User {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phone;
}
