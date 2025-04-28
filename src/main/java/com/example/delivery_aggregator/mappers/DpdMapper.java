package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.aggregator.TariffDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DpdMapper {
    @Mapping(target = "service", source = "deliveryServiceDto")
    @Mapping(target = "code", source = "serviceCost.serviceCode")
    @Mapping(target = "name", source = "serviceCost.serviceName")
    @Mapping(target = "minTime", source = "serviceCost.days")
    @Mapping(target = "maxTime", source = "serviceCost.days")
    @Mapping(target = "price", source = "serviceCost.cost")
    TariffDto serviceCostToTariffDto(ServiceCost serviceCost, DeliveryServiceDto deliveryServiceDto);

    default ArrayList<TariffDto> serviceCostListToTariffDtoList(List<ServiceCost> serviceCosts, DeliveryServiceDto deliveryServiceDto) {
        if (serviceCosts == null) {
            return new ArrayList<>();
        }

        return serviceCosts.stream()
                .map(serviceCost -> serviceCostToTariffDto(serviceCost, deliveryServiceDto))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}