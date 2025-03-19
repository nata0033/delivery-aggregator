package com.example.delivery_aggregator.dto.pecom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PecomCalculatorResponse {
    @JsonProperty("take")
    private List<String> take;

    @JsonProperty("auto")
    private List<String> auto;

    @JsonProperty("alma_auto")
    private List<String> almaAuto;

    @JsonProperty("avia")
    private List<String> avia;

    @JsonProperty("autonegabarit")
    private List<String> autonegabarit;

    @JsonProperty("ADD")
    private List<String> add;

    @JsonProperty("ADD_1")
    private List<String> add1;

    @JsonProperty("ADD_2")
    private List<String> add2;

    @JsonProperty("ADD_3")
    private List<String> add3;

    @JsonProperty("ADD_4")
    private List<String> add4;

    @JsonProperty("deliver")
    private List<String> deliver;

    @JsonProperty("periods")
    private String periods;

    @JsonProperty("aperiods")
    private String aperiods;

    @JsonProperty("error")
    private List<String> error;
}
