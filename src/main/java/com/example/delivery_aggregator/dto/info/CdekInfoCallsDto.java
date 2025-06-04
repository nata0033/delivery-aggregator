package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekInfoCallsDto {
    @JsonProperty("failed_calls")
    private List<CdekInfoFailedCallDto> failedCalls;
    @JsonProperty("rescheduled_calls")
    private List<CdekInfoRescheduledCallDto> rescheduledCalls;
}
