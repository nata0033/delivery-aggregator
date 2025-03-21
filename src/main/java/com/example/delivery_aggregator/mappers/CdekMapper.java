package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.cdek.*;
import com.example.delivery_aggregator.dto.cdek.calculator.Package;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponse;
import com.example.delivery_aggregator.dto.cdek.calculator.Location;
import com.example.delivery_aggregator.dto.cdek.calculator.TariffCode;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderRequest;
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
    Package aggrPackageToCdekPackage(com.example.delivery_aggregator.dto.aggregator.Package pack);

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
/*
    @Mapping(target = "tariffCode", source = "tariff.code")
    @Mapping(target = "sender.name", expression = "java(orderPageData.sender.toString())")
    @Mapping(target = "sender.phones", source = "sender", qualifiedByName = "phoneToPhoneList")
    @Mapping(target = "recipient.name", expression = "java(orderPageData.recipient.toString())")
    @Mapping(target = "recipient.phones", source = "recipient", qualifiedByName = "phoneToPhoneList")
    CdekOrderRequest orderPageDaratoCdekOrderRequest(OrderPageData orderPageData);

    @Named("phoneToPhoneList")
    default List<Phone> phoneToPhoneList(User user){
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phone.setNumber(user.getPhone());
        phones.add(phone);
        return phones;
    }*/
}