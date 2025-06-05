package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoReturnItemDetailDto {
    @JsonProperty("direct_order_number")
    private String directOrderNumber;
    @JsonProperty("direct_order_uuid")
    private String directOrderUuid;
    @JsonProperty("direct_package_number")
    private String directPackageNumber;
}
