package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CdekInfoPackageDto {
    private String number;
    private String barcode;
    private Integer weight;
    private Integer length;
    private Integer width;
    @JsonProperty("weight_volume")
    private Integer weightVolume;
    @JsonProperty("weight_calc")
    private Integer weightCalc;
    private Integer height;
    private String comment;
    private List<CdekInfoItemDto> items;
    private List<CdekInfoPackageServiceDto> services;
    @JsonProperty("package_id")
    private String packageId;
}
