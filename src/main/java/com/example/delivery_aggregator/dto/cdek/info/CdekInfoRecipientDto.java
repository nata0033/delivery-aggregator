package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CdekInfoRecipientDto {
    private String company;
    private String name;
    @JsonProperty("contragent_type")
    private String contragentType;
    @JsonProperty("passport_series")
    private String passportSeries;
    @JsonProperty("passport_number")
    private String passportNumber;
    @JsonProperty("passport_date_of_issue")
    private LocalDate passportDateOfIssue;
    @JsonProperty("passport_organization")
    private String passportOrganization;
    private String tin;
    @JsonProperty("passport_date_of_birth")
    private LocalDate passportDateOfBirth;
    private String email;
    private List<CdekInfoPhoneDto> phones;
    @JsonProperty("passport_requirements_satisfied")
    private Boolean passportRequirementsSatisfied;
}
