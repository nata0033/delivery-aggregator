package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.external_api.yandex.PlacePhysicalDimensions;
import com.example.delivery_aggregator.dto.external_api.yandex.PricingDestinationNode;
import com.example.delivery_aggregator.dto.external_api.yandex.PricingResourcePlace;
import com.example.delivery_aggregator.dto.pages.LocationDto;
import com.example.delivery_aggregator.dto.pages.PackageDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T15:06:42+0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class YandexMapperImpl implements YandexMapper {

    @Override
    public PricingDestinationNode locationToPricingDestinationNode(LocationDto location) {
        if ( location == null ) {
            return null;
        }

        PricingDestinationNode pricingDestinationNode = new PricingDestinationNode();

        pricingDestinationNode.setAddress( location.toString() );

        return pricingDestinationNode;
    }

    @Override
    public PlacePhysicalDimensions packageParamsToPlacePhysicalDimensions(PackageDto pack) {
        if ( pack == null ) {
            return null;
        }

        PlacePhysicalDimensions placePhysicalDimensions = new PlacePhysicalDimensions();

        placePhysicalDimensions.setDx( pack.getLength() );
        placePhysicalDimensions.setDy( pack.getHeight() );
        placePhysicalDimensions.setDz( pack.getWidth() );
        placePhysicalDimensions.setWeightGross( pack.getWeight() );

        return placePhysicalDimensions;
    }

    @Override
    public PricingResourcePlace packageToPricingResourcePlace(PackageDto pack) {
        if ( pack == null ) {
            return null;
        }

        PricingResourcePlace pricingResourcePlace = new PricingResourcePlace();

        pricingResourcePlace.setPhysicalDims( packageParamsToPlacePhysicalDimensions( pack ) );

        return pricingResourcePlace;
    }
}
