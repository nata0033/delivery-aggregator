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
import java.util.stream.IntStream;

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
    @Mapping(target = "tariffCode", source = "tariff.code")
    @Mapping(target = "sender", source = "sender", qualifiedByName = "userToContact")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "userToContact")
    @Mapping(target = "fromLocation", source = "fromLocation", qualifiedByName = "locationToOrderCdekLocation")
    @Mapping(target = "toLocation", source = "toLocation", qualifiedByName = "locationToOrderCdekLocation")
    @Mapping(target = "packages", source = "packages",  qualifiedByName = "packageDtoListToCdekOrderPackageDtoList")
    CdekOrderRequestDto OrderPageDataDtoToCdekOrderRequest(OrderPageDataDto orderPageData);

    @Named("packageDtoListToCdekOrderPackageDtoList")
    default List<CdekOrderPackageDto> packageDtoListToCdekOrderPackageDtoList (List<PackageDto> packages){
        List<CdekOrderPackageDto> cdekPackages = new ArrayList<>();
        
        return cdekPackages(
                IntStream.range(0, packages.size())
                        .mapToObj(i -> {
                            CdekOrderPackageDto p = aggregatorPackageToOrderCdekPackage(packages.get(i));
                            p.setNumber(String.valueOf(i));
                            return p;
                        }).toList()
        );
    }

    List<CdekOrderPackageDto> cdekPackages(List<CdekOrderPackageDto> list);

    @Mapping(target = "length", source = "length")
    @Mapping(target = "height", source = "height")
    @Mapping(target = "width", source = "width")
    @Mapping(target = "weight", source = "weight")
    CdekOrderPackageDto aggregatorPackageToOrderCdekPackage(PackageDto p);

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
}