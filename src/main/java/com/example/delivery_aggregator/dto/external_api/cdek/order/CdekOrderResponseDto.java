package com.example.delivery_aggregator.dto.external_api.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekOrderResponseDto {
    private CdekOrderEntityDto entity;
    private List<RequestDto> requests;
    @JsonProperty("related_entities")
    private List<CdekOrderRelatedEntityDto> relatedEntities;
}
