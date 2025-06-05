package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CdekInfoRelatedEntityDto {
    private String uuid;
    private String type;
    private String url;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("cdek_number")
    private String cdekNumber;
    private LocalDate date;
    @JsonProperty("time_from")
    private LocalTime timeFrom;
    @JsonProperty("time_to")
    private LocalTime timeTo;
}
