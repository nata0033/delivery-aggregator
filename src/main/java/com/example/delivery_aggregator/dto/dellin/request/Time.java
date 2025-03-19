package com.example.delivery_aggregator.dto.dellin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Time {
    @JsonProperty("worktimeStart")
    private String worktimeStart;
    @JsonProperty("worktimeEnd")
    private String worktimeEnd;
    //@JsonProperty("breakStart")
    //private String breakStart;
    //@JsonProperty("breakEnd")
    //private String breakEnd;
    //@JsonProperty("exactTime")
    //private Boolean exactTime;
}
