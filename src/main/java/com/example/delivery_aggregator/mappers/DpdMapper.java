package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.aggregator.ContactDto;
import com.example.delivery_aggregator.entity.DpdCity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dpd.ws.calculator._2012_03_20.Auth;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;
import ru.dpd.ws.calculator._2012_03_20.ServiceCostRequest;
import ru.dpd.ws.geography._2015_05_20.City;
import ru.dpd.ws.geography._2015_05_20.DpdCitiesCashPayRequest;
import ru.dpd.ws.order2._2012_04_04.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DpdMapper {
    //Города
    @Mapping(target = "auth.clientNumber", source = "dpdClientNumber")
    @Mapping(target = "auth.clientKey", source = "dpdClientKey")
    @Mapping(target = "countryCode", source = "countryCode")
    DpdCitiesCashPayRequest dpdAuthDataAndCountryCodeToDpdCitiesCashPayRequest(
            Long dpdClientNumber, String dpdClientKey, String countryCode);

    DpdCity cityToDpdCity(City city);

    List<DpdCity> cityListToDpdCityList(List<City> city);


    //Тарифы
    @Mapping(target = "auth.clientNumber", source = "dpdClientNumber")
    @Mapping(target = "auth.clientKey", source = "dpdClientKey")
    @Mapping(target = "pickup.cityId", source = "pickupCityId")
    @Mapping(target = "delivery.cityId", source = "deliveryCityId")
    @Mapping(target = "selfPickup", source = "indexPageDataDto.selfPickup")
    @Mapping(target = "selfDelivery", source = "indexPageDataDto.selfDelivery")
    @Mapping(target = "weight", source = "indexPageDataDto.packages")
    @Mapping(target = "pickupDate", source = "indexPageDataDto.fromLocation.date")
    ServiceCostRequest indexPageDataDtoAnddpdAuthDataToServiceCostRequest(IndexPageDataDto indexPageDataDto,
                                                                          Long dpdClientNumber, String dpdClientKey,
                                                                          Long pickupCityId, Long deliveryCityId);
    default Double getWeight(List<PackageDto> packages){
        return packages.stream().mapToDouble(PackageDto::getWeight).sum();
    }

    default XMLGregorianCalendar dateStringToXMLGregorianCalendar(String dateStr) throws DatatypeConfigurationException, ParseException {
        // Используем формат "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }


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