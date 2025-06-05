package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CdekInfoFailedCallDto {
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("reason_code")
    private Integer reasonCode;
}
