package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.external_api.cdek.CdekSuggestCityResponseDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorLocationDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorPackageDto;
import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorTariffCodeDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderContactDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderLocationDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderPackageDto;
import com.example.delivery_aggregator.dto.external_api.cdek.order.CdekOrderRequestDto;
import com.example.delivery_aggregator.dto.pages.DeliveryDataDto;
import com.example.delivery_aggregator.dto.pages.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.pages.LocationDto;
import com.example.delivery_aggregator.dto.pages.OrderPageDataDto;
import com.example.delivery_aggregator.dto.pages.PackageDto;
import com.example.delivery_aggregator.dto.pages.TariffDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T15:06:42+0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CdekMapperImpl implements CdekMapper {

    @Override
    public CdekCalculatorLocationDto suggestCityResponseToLocation(CdekSuggestCityResponseDto suggestCityResponse) {
        if ( suggestCityResponse == null ) {
            return null;
        }

        CdekCalculatorLocationDto cdekCalculatorLocationDto = new CdekCalculatorLocationDto();

        cdekCalculatorLocationDto.setCode( suggestCityResponse.getCode() );

        return cdekCalculatorLocationDto;
    }

    @Override
    public CdekCalculatorPackageDto aggregatorPackageToCalculatorCdekPackage(PackageDto pack) {
        if ( pack == null ) {
            return null;
        }

        CdekCalculatorPackageDto cdekCalculatorPackageDto = new CdekCalculatorPackageDto();

        cdekCalculatorPackageDto.setLength( pack.getLength() );
        cdekCalculatorPackageDto.setHeight( pack.getHeight() );
        cdekCalculatorPackageDto.setWidth( pack.getWidth() );
        cdekCalculatorPackageDto.setWeight( pack.getWeight() );

        return cdekCalculatorPackageDto;
    }

    @Override
    public TariffDto tariffCodeToTariff(CdekCalculatorTariffCodeDto tariffCode, DeliveryServiceDto deliveryServiceDto) {
        if ( tariffCode == null && deliveryServiceDto == null ) {
            return null;
        }

        TariffDto tariffDto = new TariffDto();

        if ( tariffCode != null ) {
            if ( tariffCode.getTariffCode() != null ) {
                tariffDto.setCode( String.valueOf( tariffCode.getTariffCode() ) );
            }
            tariffDto.setName( tariffCode.getTariffName() );
            if ( tariffCode.getDeliverySum() != null ) {
                tariffDto.setPrice( tariffCode.getDeliverySum().intValue() );
            }
            tariffDto.setMinTime( tariffCode.getPeriodMin() );
            tariffDto.setMaxTime( tariffCode.getPeriodMax() );
        }
        tariffDto.setService( deliveryServiceDto );

        return tariffDto;
    }

    @Override
    public CdekOrderRequestDto orderPageDataAndDeliveryDataToCdekOrderRequest(OrderPageDataDto orderPageData, DeliveryDataDto deliveryData) {
        if ( orderPageData == null && deliveryData == null ) {
            return null;
        }

        CdekOrderRequestDto cdekOrderRequestDto = new CdekOrderRequestDto();

        if ( orderPageData != null ) {
            cdekOrderRequestDto.setSender( userToContact( orderPageData.getSender() ) );
            cdekOrderRequestDto.setRecipient( userToContact( orderPageData.getRecipient() ) );
            cdekOrderRequestDto.setFromLocation( locationToOrderCdekLocation( orderPageData.getFromLocation() ) );
            cdekOrderRequestDto.setToLocation( locationToOrderCdekLocation( orderPageData.getToLocation() ) );
            cdekOrderRequestDto.setComment( orderPageData.getComment() );
        }
        if ( deliveryData != null ) {
            String code = deliveryDataTariffCode( deliveryData );
            if ( code != null ) {
                cdekOrderRequestDto.setTariffCode( Integer.parseInt( code ) );
            }
            cdekOrderRequestDto.setPackages( packageDtoListToCdekOrderPackageDtoList( deliveryData.getPackages() ) );
        }

        return cdekOrderRequestDto;
    }

    @Override
    public CdekOrderContactDto userToContact(ContactDto user) {
        if ( user == null ) {
            return null;
        }

        CdekOrderContactDto cdekOrderContactDto = new CdekOrderContactDto();

        cdekOrderContactDto.setPhones( phoneStringToPhones( user.getPhone() ) );
        cdekOrderContactDto.setEmail( user.getEmail() );

        cdekOrderContactDto.setName( user.getFirstName() + ' ' + user.getLastName() + ' ' + user.getFatherName() );

        return cdekOrderContactDto;
    }

    @Override
    public CdekOrderLocationDto locationToOrderCdekLocation(LocationDto location) {
        if ( location == null ) {
            return null;
        }

        CdekOrderLocationDto cdekOrderLocationDto = new CdekOrderLocationDto();

        cdekOrderLocationDto.setRegion( location.getState() );
        cdekOrderLocationDto.setCity( location.getCity() );
        cdekOrderLocationDto.setCountry( location.getCountry() );
        cdekOrderLocationDto.setPostalCode( location.getPostalCode() );

        cdekOrderLocationDto.setAddress( location.getStreet() + ' ' + location.getHouse() + ' ' + location.getApartment() );

        return cdekOrderLocationDto;
    }

    @Override
    public CdekOrderPackageDto aggregatorPackageToOrderCdekPackage(PackageDto pack, OrderPageDataDto orderPageData) {
        if ( pack == null && orderPageData == null ) {
            return null;
        }

        CdekOrderPackageDto cdekOrderPackageDto = new CdekOrderPackageDto();

        if ( pack != null ) {
            cdekOrderPackageDto.setLength( pack.getLength() );
            cdekOrderPackageDto.setHeight( pack.getHeight() );
            cdekOrderPackageDto.setWidth( pack.getWidth() );
            cdekOrderPackageDto.setWeight( pack.getWeight() );
        }
        if ( orderPageData != null ) {
            cdekOrderPackageDto.setComment( orderPageData.getComment() );
        }

        return cdekOrderPackageDto;
    }

    private String deliveryDataTariffCode(DeliveryDataDto deliveryDataDto) {
        TariffDto tariff = deliveryDataDto.getTariff();
        if ( tariff == null ) {
            return null;
        }
        return tariff.getCode();
    }

    protected CdekOrderPackageDto packageDtoToCdekOrderPackageDto(PackageDto packageDto) {
        if ( packageDto == null ) {
            return null;
        }

        CdekOrderPackageDto cdekOrderPackageDto = new CdekOrderPackageDto();

        cdekOrderPackageDto.setWeight( packageDto.getWeight() );
        cdekOrderPackageDto.setLength( packageDto.getLength() );
        cdekOrderPackageDto.setWidth( packageDto.getWidth() );
        cdekOrderPackageDto.setHeight( packageDto.getHeight() );

        return cdekOrderPackageDto;
    }

    protected List<CdekOrderPackageDto> packageDtoListToCdekOrderPackageDtoList(List<PackageDto> list) {
        if ( list == null ) {
            return null;
        }

        List<CdekOrderPackageDto> list1 = new ArrayList<CdekOrderPackageDto>( list.size() );
        for ( PackageDto packageDto : list ) {
            list1.add( packageDtoToCdekOrderPackageDto( packageDto ) );
        }

        return list1;
    }
}
