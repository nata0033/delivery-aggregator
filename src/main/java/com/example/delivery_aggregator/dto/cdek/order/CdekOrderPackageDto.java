package com.example.delivery_aggregator.dto.cdek.order;

import lombok.Data;

import java.util.List;

@Data
public class CdekOrderPackageDto {
    private String number;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
    private String comment;
    private List<Object> items;
}