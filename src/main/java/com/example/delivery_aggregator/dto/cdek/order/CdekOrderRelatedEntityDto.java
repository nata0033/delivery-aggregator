package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekOrderRelatedEntityDto {
    @JsonProperty("uuid")
    private String uuid;

    private String type;

    private String url;

    @JsonProperty("create_time")
    private String createTime;

    @JsonProperty("cdek_number")
    private String cdekNumber;

    private String date;

    @JsonProperty("time_from")
    private String timeFrom;

    @JsonProperty("time_to")
    private String timeTo;
}
