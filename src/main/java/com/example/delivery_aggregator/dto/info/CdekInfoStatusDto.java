package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CdekInfoStatusDto {
    private String code;
    private String name;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("reason_code")
    private String reasonCode;
    private String city;
    @JsonProperty("city_uuid")
    private String cityUuid;
    private Boolean deleted;
}
