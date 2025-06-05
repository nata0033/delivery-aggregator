package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekInfoResponseDto {
    private CdekInfoEntityDto entity;
    private List<CdekInfoRequestDto> requests;
    @JsonProperty("related_entities")
    private List<CdekInfoRelatedEntityDto> relatedEntities;
}
