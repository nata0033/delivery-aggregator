package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorPackageDto;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorTariffCodeDto;
import com.example.delivery_aggregator.dto.cdek.order.*;
import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.pages.*;
import com.example.delivery_aggregator.dto.cdek.*;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.cdek.calculator.CdekCalculatorLocationDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.DeliveryService;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = AggregatorMapper.class)
public interface CdekMapper {

    //Калькулятор
    CdekCalculatorLocationDto suggestCityResponseToLocation(CdekSuggestCityResponseDto suggestCityResponse);

    @Mapping(target = "length", source = "pack.length")
    @Mapping(target = "height", source = "pack.height")
    @Mapping(target = "width", source = "pack.width")
    @Mapping(target = "weight", source = "pack.weight")
    CdekCalculatorPackageDto aggregatorPackageToCalculatorCdekPackage(PackageDto pack);

    //Тарифы
    @Named("cdekCalculatorResponseToTariffsPageData")
    @Mapping(target = "code", source = "tariffCode")
    @Mapping(target = "name", source = "tariffName")
    @Mapping(target = "price", source = "deliverySum")
    @Mapping(target = "minTime", source = "periodMin")
    @Mapping(target = "maxTime", source = "periodMax")
    TariffDto tariffCodeToTariff(CdekCalculatorTariffCodeDto tariffCode);

    @Mapping(target = "tariffs", source = "cdekCalculatorResponse.tariffCodes", qualifiedByName = "cdekCalculatorResponseToTariffsPageData")
    @Mapping(target = "fromLocation", source = "indexPageDataDto.fromLocation")
    @Mapping(target = "toLocation", source = "indexPageDataDto.toLocation")
    @Mapping(target = "packageQuantity", expression = "java((int) indexPageDataDto.getPackages().stream().count())")
    @Mapping(target = "packageWeight",  expression = "java(indexPageDataDto.getPackages().stream().mapToInt(p -> p.getWeight()).sum())")
    TariffsPageData cdekCalculatorResponseToTariffsPageData(CdekCalculatorResponseDto cdekCalculatorResponse, IndexPageDataDto indexPageDataDto);

    //Заказ
    @Mapping(target = "tariffCode", source = "deliveryData.tariff.code")
    @Mapping(target = "sender", source = "orderPageData.sender", qualifiedByName = "userToContact")
    @Mapping(target = "recipient", source = "orderPageData.recipient", qualifiedByName = "userToContact")
    @Mapping(target = "fromLocation", source = "orderPageData.fromLocation", qualifiedByName = "locationToOrderCdekLocation")
    @Mapping(target = "toLocation", source = "orderPageData.toLocation", qualifiedByName = "locationToOrderCdekLocation")
    CdekOrderRequestDto orderPageDataAndDeliveryDataToCdekOrderRequest(OrderPageDataDto orderPageData, DeliveryDataDto deliveryData);

    @Named("userToContact")
    @Mapping(target = "name", expression = "java(user.getFirstName() + ' ' + user.getLastName() + ' ' + user.getFatherName())")
    @Mapping(target = "phones", source = "phone", qualifiedByName = "phoneStringToPhones")
    CdekOrderContactDto userToContact(ContactDto user);

    @Named("phoneStringToPhones")
    default List<CdekOrderPhoneDto> phoneStringToPhones(String userPhone){
        CdekOrderPhoneDto phone = new CdekOrderPhoneDto();
        phone.setNumber(userPhone);
        List<CdekOrderPhoneDto> phonies = new ArrayList<>();
        phonies.add(phone);
        return phonies;
    }

    @Named("locationToOrderCdekLocation")
    @Mapping(target = "region", source = "state")
    @Mapping(target = "address", expression = "java(location.getStreet() + ' ' + location.getHouse() + ' ' + location.getApartment())")
    CdekOrderLocationDto locationToOrderCdekLocation(LocationDto location);

    @Mapping(target = "length", source = "pack.length")
    @Mapping(target = "height", source = "pack.height")
    @Mapping(target = "width", source = "pack.width")
    @Mapping(target = "weight", source = "pack.weight")
    @Mapping(target = "comment", source = "orderPageData.comment")
    CdekOrderPackageDto aggregatorPackageToOrderCdekPackage(PackageDto pack, OrderPageDataDto orderPageData);

}