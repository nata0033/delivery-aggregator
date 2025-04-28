package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.external_api.cdek.CdekSuggestCityResponseDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorPackageDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorTariffCodeDto;
import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorLocationDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.*;
import com.example.delivery_aggregator.dto.aggregator.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Mapping(target = "service", source = "deliveryServiceDto")
    @Mapping(target = "code", source = "tariffCode.tariffCode")
    @Mapping(target = "name", source = "tariffCode.tariffName")
    @Mapping(target = "price", source = "tariffCode.deliverySum")
    @Mapping(target = "minTime", source = "tariffCode.periodMin")
    @Mapping(target = "maxTime", source = "tariffCode.periodMax")
    TariffDto tariffCodeToTariff(CdekCalculatorTariffCodeDto tariffCode, DeliveryServiceDto deliveryServiceDto);

    default ArrayList<TariffDto> cdekCalculatorResponseDtoToTariffDtoList(CdekCalculatorResponseDto cdekCalculatorResponseDto, DeliveryServiceDto deliveryServiceDto){
        if (cdekCalculatorResponseDto == null || cdekCalculatorResponseDto.getTariffCodes() == null) {
            return new ArrayList<>();
        }

        return cdekCalculatorResponseDto.getTariffCodes().stream()
                .map(tariffCode -> tariffCodeToTariff(tariffCode, deliveryServiceDto))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Заказ
    @Mapping(target = "tariffCode", source = "deliveryData.tariff.code")
    @Mapping(target = "sender", source = "orderPageData.sender", qualifiedByName = "userToContact")
    @Mapping(target = "recipient", source = "orderPageData.recipient", qualifiedByName = "userToContact")
    @Mapping(target = "fromLocation", source = "orderPageData.fromLocation", qualifiedByName = "locationToOrderCdekLocation")
    @Mapping(target = "toLocation", source = "orderPageData.toLocation", qualifiedByName = "locationToOrderCdekLocation")
    CdekOrderRequestDto orderPageDataAndDeliveryDataToCdekOrderRequest(OrderPageDataDto orderPageData, CookieDeliveryDataDto deliveryData);

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