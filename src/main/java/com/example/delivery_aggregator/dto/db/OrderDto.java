package com.example.delivery_aggregator.dto.db;

import com.example.delivery_aggregator.dto.aggregator.PackageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String serviceName;
    private String number;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String date;
    private String status;
    private Integer price;
    private ContactDto recipient;
    private String fromLocation;
    private String toLocation;
    private List<PackageDto> packages;
}
