package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.external_api.dpd.DpdLocationDto;
import com.example.delivery_aggregator.dto.external_api.dpd.DpdServiceCostRequestDto;
import com.example.delivery_aggregator.dto.pages.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pages.LocationDto;
import com.example.delivery_aggregator.dto.pages.TariffDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T15:42:26+0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class DpdMapperImpl implements DpdMapper {

    @Override
    public DpdServiceCostRequestDto indexPageDataDtoToDpdServiceCostRequestDto(IndexPageDataDto indexPageDataDto) {
        if ( indexPageDataDto == null ) {
            return null;
        }

        DpdServiceCostRequestDto dpdServiceCostRequestDto = new DpdServiceCostRequestDto();

        dpdServiceCostRequestDto.setPickup( locationDtoToDpdLocationDto( indexPageDataDto.getFromLocation() ) );
        dpdServiceCostRequestDto.setDelivery( locationDtoToDpdLocationDto1( indexPageDataDto.getToLocation() ) );

        dpdServiceCostRequestDto.setWeight( (double) indexPageDataDto.getPackages().stream().mapToInt(p->p.getWeight()).sum() );

        return dpdServiceCostRequestDto;
    }

    @Override
    public TariffDto ServiceCostToTariffDto(ServiceCost serviceCost, DeliveryServiceDto deliveryServiceDto) {
        if ( serviceCost == null && deliveryServiceDto == null ) {
            return null;
        }

        TariffDto tariffDto = new TariffDto();

        if ( serviceCost != null ) {
            tariffDto.setCode( serviceCost.getServiceCode() );
            tariffDto.setName( serviceCost.getServiceName() );
            tariffDto.setMinTime( serviceCost.getDays() );
            tariffDto.setMaxTime( serviceCost.getDays() );
            if ( serviceCost.getCost() != null ) {
                tariffDto.setPrice( serviceCost.getCost().intValue() );
            }
        }
        tariffDto.setService( deliveryServiceDto );

        return tariffDto;
    }

    protected DpdLocationDto locationDtoToDpdLocationDto(LocationDto locationDto) {
        if ( locationDto == null ) {
            return null;
        }

        DpdLocationDto dpdLocationDto = new DpdLocationDto();

        dpdLocationDto.setCityName( locationDto.getCity() );

        return dpdLocationDto;
    }

    protected DpdLocationDto locationDtoToDpdLocationDto1(LocationDto locationDto) {
        if ( locationDto == null ) {
            return null;
        }

        DpdLocationDto dpdLocationDto = new DpdLocationDto();

        dpdLocationDto.setCityName( locationDto.getCity() );

        return dpdLocationDto;
    }
}
