package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.DeliveryData;
import com.example.delivery_aggregator.dto.aggregator.OrderPageData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AggregatorMapper {

    OrderPageData deliveryDataToOrderPageData(DeliveryData deliveryData);

}
