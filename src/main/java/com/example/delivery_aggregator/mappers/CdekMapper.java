package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.cdek.CdekSuggestCityResponseDto;
import com.example.delivery_aggregator.dto.cdek.calculator.*;
import com.example.delivery_aggregator.dto.aggregator.ContactDto;
import com.example.delivery_aggregator.dto.cdek.order.*;
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
    @Mapping(target = "fromLocation", source = "suggestFromCityResponse")
    @Mapping(target = "toLocation", source = "suggestToCityResponse")
    @Mapping(target = "packages", source = "indexPageDataDto.packages")
    CdekCalculatorRequestDto indexPageDataDtoAndCdekSuggestCityResponseDtoToCdekCalculatorRequestDto(
            IndexPageDataDto indexPageDataDto, CdekSuggestCityResponseDto suggestFromCityResponse,
            CdekSuggestCityResponseDto suggestToCityResponse);

    CdekCalculatorLocationDto suggestCityResponseToLocation(CdekSuggestCityResponseDto suggestCityResponse);

    CdekCalculatorPackageDto packageDtoToCdekCalculatorPackageDto(PackageDto p);

    List<CdekCalculatorPackageDto> packageDtoListToCdekCalculatorPackageDtoList(List<PackageDto> packages);

    //Тарифы
    @Mapping(target = "service", source = "deliveryServiceDto")
    @Mapping(target = "code", source = "tariffCode.tariffCode")
    @Mapping(target = "name", source = "tariffCode.tariffName")
    @Mapping(target = "price", source = "tariffCode.deliverySum")
    @Mapping(target = "minTime", source = "tariffCode.periodMin")
    @Mapping(target = "maxTime", source = "tariffCode.periodMax")
    TariffDto tariffCodeToTariff(CdekCalculatorTariffCodeDto tariffCode, DeliveryServiceDto deliveryServiceDto);

    default ArrayList<TariffDto> cdekCalculatorResponseDtoToTariffDtoList(CdekCalculatorResponseDto cdekCalculatorResponseDto,
                                                                          DeliveryServiceDto deliveryServiceDto){
        if (cdekCalculatorResponseDto == null || cdekCalculatorResponseDto.getTariffCodes() == null) {
            return new ArrayList<>();
        }

        return cdekCalculatorResponseDto.getTariffCodes().stream()
                .map(tariffCode -> tariffCodeToTariff(tariffCode, deliveryServiceDto))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Заказ
    @Mapping(target = "type", constant = "2")
    @Mapping(target = "tariffCode", source = "tariff.code")
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "fromLocation", source = "fromLocation")
    @Mapping(target = "toLocation", source = "toLocation")
    @Mapping(target = "packages", source = "packages")
    CdekOrderRequestDto OrderPageDataDtoToCdekOrderRequest(OrderPageDataDto orderPageData);

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

    @Mapping(target = "comment", constant = "no comment")
    CdekOrderPackageDto aggregatorPackageToOrderCdekPackage(PackageDto p);

    @Mapping(target = "name", expression = "java(user.getFirstName() + ' ' + user.getLastName() + ' ' + user.getFatherName())")
    @Mapping(target = "phones", source = "phone")
    @Mapping(target = "company", constant = "DeliveryAggregator")
    CdekOrderContactDto userToContact(ContactDto user);

    default List<CdekOrderPhoneDto> phoneStringToPhones(String userPhone){
        CdekOrderPhoneDto phone = new CdekOrderPhoneDto();
        phone.setNumber(userPhone);
        List<CdekOrderPhoneDto> phonies = new ArrayList<>();
        phonies.add(phone);
        return phonies;
    }

    @Mapping(target = "region", source = "state")
    @Mapping(target = "address", expression = "java(location.getStreet() + ' ' + location.getHouse() + ' ' + location.getApartment())")
    CdekOrderLocationDto locationToOrderCdekLocation(LocationDto location);
}