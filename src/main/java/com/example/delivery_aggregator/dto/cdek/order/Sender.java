package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Sender {
    private String company;
    private String name;
    @JsonProperty("contragent_type")
    private String contragentType;
    @JsonProperty("passport_series")
    private String passportSeries;
    @JsonProperty("passport_number")
    private String passportNumber;
    @JsonProperty("passport_date_of_issue")
    private String passportDateOfIssue;
    @JsonProperty("passport_organization")
    private String passportOrganization;
    private String tin;
    @JsonProperty("passport_date_of_birth")
    private String passportDateOfBirth;
    private String email;
    private List<Object> phones;
}
