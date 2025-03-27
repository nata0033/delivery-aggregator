package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.pages.LocationDto;
import com.example.delivery_aggregator.dto.pages.PackageDto;
import com.example.delivery_aggregator.dto.pages.PackageParamsDto;
import com.example.delivery_aggregator.dto.yandex.PlacePhysicalDimensions;
import com.example.delivery_aggregator.dto.yandex.PricingDestinationNode;
import com.example.delivery_aggregator.dto.yandex.PricingResourcePlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface YandexMapper {

    @Mapping(target = "address", expression = "java(location.toString())")
    PricingDestinationNode locationToPricingDestinationNode(LocationDto location);

    @Mapping(target = "dx", source = "length")
    @Mapping(target = "dy", source = "height")
    @Mapping(target = "dz", source = "width")
    @Mapping(target = "weightGross", source = "weight")
    PlacePhysicalDimensions packageParamsToPlacePhysicalDimensions(PackageParamsDto pack);

    @Mapping(target = "physicalDims", source = "packageParams")
    PricingResourcePlace packageToPricingResourcePlace(PackageDto pack);
}