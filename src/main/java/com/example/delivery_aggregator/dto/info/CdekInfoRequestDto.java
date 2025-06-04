package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CdekInfoRequestDto {
    @JsonProperty("request_uuid")
    private String requestUuid;
    private String type;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    private String state;
    private List<CdekInfoErrorDto> errors;
    private List<CdekInfoWarningDtoDto> warnings;
}
