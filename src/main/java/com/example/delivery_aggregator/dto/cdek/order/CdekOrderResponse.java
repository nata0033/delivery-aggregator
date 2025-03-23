package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekOrderResponse {
    private Entity entity;
    private List<Request> requests;
    @JsonProperty("related_entities")
    private List<RelatedEntity> relatedEntities;
}
