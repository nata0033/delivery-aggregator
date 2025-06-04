package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekInfoRootDto {
    private CdekInfoEntityDto entity;
    private List<CdekInfoRequestDto> requests;
    @JsonProperty("related_entities")
    private List<CdekInfoRelatedEntityDto> relatedEntities;
}
