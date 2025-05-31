package com.example.delivery_aggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "delivery_aggregator", name = "dpd_city")
public class DpdCity {

    @Id
    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "region_code")
    private Integer regionCode;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "index_min")
    private String indexMin;

    @Column(name = "index_max")
    private String indexMax;
}
