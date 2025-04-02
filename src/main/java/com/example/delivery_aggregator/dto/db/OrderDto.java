package com.example.delivery_aggregator.dto.db;

import com.example.delivery_aggregator.dto.pages.PackageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private String serviceName;
    private String number;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;
    private String status;
    private Integer price;
    private ContactDto recipient;
    private List<PackageDto> packages;
}
