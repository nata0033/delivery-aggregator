package com.example.delivery_aggregator.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CdekInfoLocationDto {
    private Integer code;
    @JsonProperty("city_uuid")
    private String cityUuid;
    private String city;
    @JsonProperty("fias_guid")
    private String fiasGuid;
    @JsonProperty("kladr_code")
    private String kladrCode;
    @JsonProperty("country_code")
    private String countryCode;
    private String country;
    private String region;
    @JsonProperty("region_code")
    private Integer regionCode;
    @JsonProperty("fias_region_guid")
    private String fiasRegionGuid;
    @JsonProperty("sub_region")
    private String subRegion;
    private Double longitude;
    private Double latitude;
    @JsonProperty("time_zone")
    private String timeZone;
    @JsonProperty("payment_limit")
    private Double paymentLimit;
    private String address;
    @JsonProperty("postal_code")
    private String postalCode;
}
