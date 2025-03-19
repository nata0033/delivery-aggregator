package com.example.delivery_aggregator.dto.dellin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AvailableDeliveryTypes{
    @JsonProperty("auto")
    private Float auto;
    @JsonProperty("small")
    private Float small;
    @JsonProperty("avia")
    private Float avia;
    @JsonProperty("express")
    private Float express;
    @JsonProperty("letter")
    private Float letter;
}
