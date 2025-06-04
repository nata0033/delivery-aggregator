package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoSellerDto {
    private String name;
    private String inn;
    private String phone;
    @JsonProperty("ownership_form")
    private String ownershipForm;
    private String address;
}
