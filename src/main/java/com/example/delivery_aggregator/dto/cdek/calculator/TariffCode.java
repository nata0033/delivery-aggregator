package com.example.delivery_aggregator.dto.cdek.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TariffCode {
    @JsonProperty("tariff_code")
    private Integer tariffCode;

    @JsonProperty("tariff_name")
    private String tariffName;

    @JsonProperty("tariff_description")
    private String tariffDescription;

    @JsonProperty("delivery_mode")
    private Integer deliveryMode;

    @JsonProperty("delivery_sum")
    private Float deliverySum;

    @JsonProperty("period_min")
    private Integer periodMin;

    @JsonProperty("period_max")
    private Integer periodMax;

    @JsonProperty("calendar_min")
    private Integer calendarMin;

    @JsonProperty("calendar_max")
    private Integer calendarMax;
}
