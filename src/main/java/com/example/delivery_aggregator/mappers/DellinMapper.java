package com.example.delivery_aggregator.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface DellinMapper {
//    @Named("deliveryParamsToCargo")
//    @Mapping(target = "length", expression = "java((float)formDeliveryParams.getPackages().stream().mapToInt(p->p.getPackageParams().getLength()).max().orElse(0))")
//    @Mapping(target = "width", expression = "java((float)formDeliveryParams.getPackages().stream().mapToInt(p->p.getPackageParams().getWidth()).max().orElse(0))")
//    @Mapping(target = "height", expression = "java((float)formDeliveryParams.getPackages().stream().mapToInt(p->p.getPackageParams().getHeight()).max().orElse(0))")
//    @Mapping(target = "totalVolume", expression = "java((float)formDeliveryParams.getPackages().stream().mapToInt(p->p.getPackageParams().getLength() + p.getPackageParams().getHeight() + p.getPackageParams().getWidth()).sum())")
//    @Mapping(target = "totalWeight", expression = "java((float)formDeliveryParams.getPackages().stream().mapToInt(p->p.getPackageParams().getWeight()).max().orElse(0))")
//    @Mapping(target = "hazardClass", ignore = true, defaultValue = "0")
//    Cargo deliveryParamsToCargo(IndexPageDataDto formDeliveryParams);
//
//    @Named("time")
//    @Mapping(target = "worktimeStart", ignore = true, defaultValue = "08:00")
//    @Mapping(target = "worktimeEnd", ignore = true, defaultValue = "20:00")
//    Time time(LocationDto location);
//
//    @Named("locationToAdress")
//    @Mapping(target = "search", expression = "java(location.toString())")
//    Address locationToAdress(LocationDto location);
//
//    @Named("locationToDerivalArrival")
//    @Mapping(target = "produceDate", source = "date")
//    @Mapping(target = "variant", ignore = true, defaultValue = "auto")
//    @Mapping(target = "address", ignore = true, qualifiedByName = "locationToAdress")
//    @Mapping(target = "time",  ignore = true, qualifiedByName = "time")
//    DerivalArrival locationToDerivalArrival(LocationDto location);
//
//    @Named("deliveryType")
//    @Mapping(target = "type", ignore = true, defaultValue = "auto")
//    DeliveryType deliveryType(IndexPageDataDto formDeliveryParams);
//
//    @Named("deliveryParamsToDellinCalculatorRequest")
//    @Mapping(target = "deliveryType", ignore = true, qualifiedByName = "deliveryType")
//    @Mapping(target = "arrival",source = "fromLocation", qualifiedByName = "locationToDerivalArrival")
//    @Mapping(target = "derival", source = "toLocation", qualifiedByName = "locationToDerivalArrival")
//    Delivery deliveryParamsToDellinCalculatorRequest(IndexPageDataDto formDeliveryParams);
//
//    @Mapping(target = "delivery", ignore = true, qualifiedByName = "locationToDerivalArrival")
//    @Mapping(target = "cargo", ignore = true, qualifiedByName = "deliveryParamsToDellinCalculatorRequest")
//    DellinCalculatorRequest dellinCalculatorRequest(IndexPageDataDto formDeliveryParams);
}
