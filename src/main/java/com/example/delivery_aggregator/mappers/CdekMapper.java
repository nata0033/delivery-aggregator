package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.cdek.*;
import com.example.delivery_aggregator.dto.cdek.calculator.Package;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.dto.cdek.calculator.Location;
import com.example.delivery_aggregator.dto.cdek.calculator.TariffCode;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderRequest;
import com.example.delivery_aggregator.dto.cdek.order.Contact;
import com.example.delivery_aggregator.dto.cdek.order.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CdekMapper {

    //Калькулятор
    Location suggestCityResponseToLocation(SuggestCityResponse suggestCityResponse);

    @Mapping(target = "length", source = "packageParams.length")
    @Mapping(target = "height", source = "packageParams.height")
    @Mapping(target = "width", source = "packageParams.width")
    @Mapping(target = "weight", source = "packageParams.weight")
    Package aggregatorPackageToCalculatorCdekPackage(com.example.delivery_aggregator.dto.aggregator.Package pack);

    //Тарифы
    @Named("cdekCalculatorResponseToTariffsPageData")
    @Mapping(target = "code", source = "tariffCode")
    @Mapping(target = "name", source = "tariffName")
    @Mapping(target = "price", source = "deliverySum")
    @Mapping(target = "minTime", source = "periodMin")
    @Mapping(target = "maxTime", source = "periodMax")
    Tariff tariffCodeToTariff(TariffCode tariffCode);

    @Mapping(target = "tariffs", source = "cdekCalculatorResponse.tariffCodes", qualifiedByName = "cdekCalculatorResponseToTariffsPageData")
    @Mapping(target = "fromLocation", source = "deliveryParams.fromLocation")
    @Mapping(target = "toLocation", source = "deliveryParams.toLocation")
    @Mapping(target = "packageQuantity", expression = "java((int) deliveryParams.getPackages().stream().count())")
    @Mapping(target = "packageWeight",  expression = "java(deliveryParams.getPackages().stream().mapToInt(p -> p.getPackageParams().getWeight()).sum())")
    TariffsPageData cdekCalculatorResponseToTariffsPageData(CdekCalculatorResponse cdekCalculatorResponse, IndexPageData deliveryParams);

    //Заказ
    @Mapping(target = "tariffCode", source = "deliveryData.tariff.code")
    @Mapping(target = "sender", source = "orderPageData.sender", qualifiedByName = "userToContact")
    @Mapping(target = "recipient", source = "orderPageData.recipient", qualifiedByName = "userToContact")
    @Mapping(target = "fromLocation", source = "orderPageData.fromLocation", qualifiedByName = "locationToOrderCdekLocation")
    @Mapping(target = "toLocation", source = "orderPageData.toLocation", qualifiedByName = "locationToOrderCdekLocation")
    CdekOrderRequest orderPageDataAndDeliveryDataToCdekOrderRequest(OrderPageData orderPageData, DeliveryData deliveryData);

    @Named("userToContact")
    @Mapping(target = "name", expression = "java(user.getFirstName() + ' ' + user.getLastName() + ' ' + user.getFatherName())")
    @Mapping(target = "phones", source = "phone", qualifiedByName = "phoneStringToPhones")
    Contact userToContact(User user);

    @Named("phoneStringToPhones")
    default List<Phone> phoneStringToPhones(String userPhone){
        Phone phone = new Phone();
        phone.setNumber(userPhone);
        List<Phone> phonies = new ArrayList<>();
        phonies.add(phone);
        return phonies;
    }

    @Named("locationToOrderCdekLocation")
    @Mapping(target = "region", source = "state")
    @Mapping(target = "address", expression = "java(location.getStreet() + ' ' + location.getHouse() + ' ' + location.getApartment())")
    com.example.delivery_aggregator.dto.cdek.order.Location locationToOrderCdekLocation(com.example.delivery_aggregator.dto.aggregator.Location location);

    @Mapping(target = "length", source = "pack.packageParams.length")
    @Mapping(target = "height", source = "pack.packageParams.height")
    @Mapping(target = "width", source = "pack.packageParams.width")
    @Mapping(target = "weight", source = "pack.packageParams.weight")
    @Mapping(target = "comment", source = "orderPageData.comment")
    com.example.delivery_aggregator.dto.cdek.order.Package aggregatorPackageToOrderCdekPackage(com.example.delivery_aggregator.dto.aggregator.Package pack, OrderPageData orderPageData);
}