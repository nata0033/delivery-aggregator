package com.example.delivery_aggregator.dto.external_api.cdek.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekCalculatorResponseDto {
    @JsonProperty("tariff_codes")
    private List<CdekCalculatorTariffCodeDto> tariffCodes;
}