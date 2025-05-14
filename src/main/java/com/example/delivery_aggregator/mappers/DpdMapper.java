package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.db.ContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;
import ru.dpd.ws.order2._2012_04_04.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DpdMapper {
    //Тарифы
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

    // Заказ
    @Mapping(target = "auth.clientNumber", source = "clientNumber")
    @Mapping(target = "auth.clientKey", source = "clientKey")
    @Mapping(target = "header", source = "orderPageDataDto")
    @Mapping(target = "order", source = "orderPageDataDto")
    DpdOrdersData OrderPageDataDtoAndAuthParamsToDpdOrdersData(
            OrderPageDataDto orderPageDataDto, Long clientNumber, String clientKey
    );

    @Mapping(target = "datePickup", source = "fromLocation.date")
    @Mapping(target = "pickupTimePeriod", constant = "9-18")
    @Mapping(target = "senderAddress", expression = "java(locationAndContactToAddress(orderPageDataDto.getFromLocation(), orderPageDataDto.getSender()))")
    Header OrderPageDataDtoToHeader(OrderPageDataDto orderPageDataDto);

    @Mapping(target = "orderNumberInternal", constant = "123456")
    @Mapping(target = "serviceCode", source = "tariff.code")
    @Mapping(target = "serviceVariant", constant = "ДД")
    @Mapping(target = "cargoNumPack", expression = "java(orderPageDataDto.getPackages().size())")
    @Mapping(target = "cargoWeight", expression = "java(calculateTotalWeight(orderPageDataDto))")
    @Mapping(target = "cargoVolume", expression = "java(calculateTotalVolume(orderPageDataDto))")
    @Mapping(target = "cargoRegistered", constant = "false")
    @Mapping(target = "cargoCategory", constant = "Посылка")
    @Mapping(target = "cargoValue", constant = "100.0")
    @Mapping(target = "receiverAddress", source = "toLocation")
    Order orderPageDataDtoToOrder(OrderPageDataDto orderPageDataDto);

    default List<Order> orderDtoListToOrderList(OrderPageDataDto orderPageDataDto){
        List<Order> orders = new ArrayList<>();
        Order order = orderPageDataDtoToOrder(orderPageDataDto);
        orders.add(order);
        return orders;
    }

    @Mapping(target = "city", source = "location.city")
    @Mapping(target = "street", source = "location.street")
    @Mapping(target = "house", source = "location.house")
    @Mapping(target = "flat", source = "location.apartment")
    @Mapping(target = "index", source = "location.postalCode")
    @Mapping(target = "contactFio", expression = "java(concatFio(contact))")
    @Mapping(target = "name", expression = "java(concatFio(contact))")
    @Mapping(target = "contactPhone", source = "contact.phone")
    @Mapping(target = "contactEmail", source = "contact.email")
    Address locationAndContactToAddress(LocationDto location, ContactDto contact);

    default String concatFio(ContactDto contact) {
        return String.join(" ",
                contact.getLastName() != null ? contact.getLastName() : "",
                contact.getFirstName() != null ? contact.getFirstName() : "",
                contact.getFatherName() != null ? contact.getFatherName() : "").trim();
    }

    default Integer calculateTotalWeight(OrderPageDataDto orderPageDataDto) {
        return orderPageDataDto.getPackages().stream().mapToInt(PackageDto::getWeight).sum();
    }

    default Double calculateTotalVolume(OrderPageDataDto dto) {
        return dto.getPackages().stream()
                .mapToDouble(p -> p.getLength() * p.getWidth() * p.getHeight() / 1_000_000.0)
                .sum();
    }
}