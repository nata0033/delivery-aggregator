package com.example.delivery_aggregator.dto.cdek.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekCalculatorResponse {
    @JsonProperty("tariff_codes")
    private List<TariffCode> tariffCodes;
}