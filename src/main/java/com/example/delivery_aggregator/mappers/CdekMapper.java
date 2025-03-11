package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.cdek.Location;
import com.example.delivery_aggregator.dto.cdek.SuggestCityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CdekMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(target = "postalCode", ignore = true)
    @Mapping(target = "countryCode", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "contragentType", ignore = true)
    Location suggestCityResponseToLocation(SuggestCityResponse suggestCityResponse);
}
