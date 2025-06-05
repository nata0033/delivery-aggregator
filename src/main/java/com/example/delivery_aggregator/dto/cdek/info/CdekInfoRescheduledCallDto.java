package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CdekInfoRescheduledCallDto {
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("date_next")
    private LocalDate dateNext;
    @JsonProperty("time_next")
    private LocalTime timeNext;
    private String comment;
}
