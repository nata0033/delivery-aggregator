package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.external_api.dpd.DpdServiceCostRequestDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DpdMapper {

    @Mapping(target = "pickup.cityName", source = "indexPageDataDto.fromLocation.city")
    @Mapping(target = "delivery.cityName", source = "indexPageDataDto.toLocation.city")
    @Mapping(target = "weight", expression = "java((double) indexPageDataDto.getPackages().stream().mapToInt(p->p.getWeight()).sum())")
    DpdServiceCostRequestDto indexPageDataDtoToDpdServiceCostRequestDto(IndexPageDataDto indexPageDataDto);

    default void test(){
    }
}