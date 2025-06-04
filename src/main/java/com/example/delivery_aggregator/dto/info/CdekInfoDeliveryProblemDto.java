package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CdekInfoDeliveryProblemDto {
    private String code;
    @JsonProperty("create_date")
    private LocalDateTime createDate;
}
