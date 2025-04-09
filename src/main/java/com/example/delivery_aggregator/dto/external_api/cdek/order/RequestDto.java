package com.example.delivery_aggregator.dto.external_api.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestDto {
    @JsonProperty("request_uuid")
    private String requestUuid;

    private String type;

    @JsonProperty("date_time")
    private String dateTime;

    private String state;

    private List<CdekOrderErrorDto> errors;

    private List<CdekOrderWarningDto> warnings;
}
