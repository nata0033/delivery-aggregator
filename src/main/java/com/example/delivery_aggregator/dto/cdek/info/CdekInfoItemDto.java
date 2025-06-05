package com.example.delivery_aggregator.dto.cdek.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoItemDto {
    private String name;
    @JsonProperty("ware_key")
    private String wareKey;
    private String marking;
    private CdekInfoPaymentDto payment;
    private Integer weight;
    @JsonProperty("weight_gross")
    private Integer weightGross;
    private Integer amount;
    @JsonProperty("delivery_amount")
    private Integer deliveryAmount;
    @JsonProperty("name_i18n")
    private String nameI18n;
    private String brand;
    @JsonProperty("country_code")
    private String countryCode;
    private String material;
    @JsonProperty("wifi_gsm")
    private Boolean wifiGsm;
    private String url;
    @JsonProperty("return_item_detail")
    private CdekInfoReturnItemDetailDto returnItemDetail;
    private Boolean excise;
    private Double cost;
    @JsonProperty("feacn_code")
    private String feacnCode;
    @JsonProperty("jewel_uin")
    private String jewelUin;
}
