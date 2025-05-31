package com.example.delivery_aggregator.dto.cdek.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekOrderSellerDto {
    private String name;
    private String inn;
    private String phone;
    @JsonProperty("ownership_form")
    private String ownershipForm;
    private String address;
}

