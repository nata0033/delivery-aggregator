package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.db.OrderDto;
import com.example.delivery_aggregator.dto.pages.*;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.Package;
import com.example.delivery_aggregator.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AggregatorMapper {

    @Mapping(target = "sender", source = "contact")
    @Mapping(target = "fromLocation", source = "deliveryData.fromLocation")
    @Mapping(target = "toLocation", source = "deliveryData.toLocation")
    @Mapping(target = "tariff", source = "deliveryData.tariff")
    OrderPageDataDto deliveryDataToOrderPageData(DeliveryDataDto deliveryData, Contact contact);

    Contact registrationPageToContact(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "login", source = "email")
    User registrationPageToUser(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "user", source = "contact")
    @Mapping(target = "addresses", source = "contact.addresses")
    @Mapping(target = "orders", source = "user.sentOrders")
    @Mapping(target = "contacts", source = "user.contacts")
    @Mapping(target = "orderQuantity", expression = "java((int)user.getSentOrders().stream().count())")
    AccountPageDataDto contactToAccountPageDataDto(User user, Contact contact);

    Contact contactDtoToContact(ContactDto contactDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceOrderNumber", source = "serviceOrderNumber")
    @Mapping(target = "price", source = "orderPageDataDto.tariff.price")
    @Mapping(target = "fromLocation", source = "orderPageDataDto.fromLocation", qualifiedByName = "locationDtoToLocationString")
    @Mapping(target = "toLocation", source = "orderPageDataDto.toLocation", qualifiedByName = "locationDtoToLocationString")
    @Mapping(target = "contact", source = "orderPageDataDto.recipient")
    Order orderPageDataDtoToOrder(OrderPageDataDto orderPageDataDto, String serviceOrderNumber);

    @Named("locationDtoToLocationString")
    default String locationDtoToLocationString(LocationDto locationDto) {
        return locationDto.getCity() + " " + locationDto.getStreet() + " " + locationDto.getHouse() + " " + locationDto.getApartment();
    }

    @Mapping(target = "id", ignore = true)
    Package packageDtoToPackage(PackageDto packageDto);

    default List<Package> packagesDtoToPackages(List<PackageDto> packagesDto, Order order){
        List<Package> packages = new ArrayList<>();

        for(PackageDto packageDto : packagesDto) {
            Package pack = packageDtoToPackage(packageDto);
            pack.setOrder(order);
            packages.add(pack);
        }
        return packages;
    }

    @Mapping(target = "serviceName", source = "deliveryService.name")
    @Mapping(target = "number", source = "serviceOrderNumber")
    @Mapping(target = "date", source = "createdAt")
    @Mapping(target = "recipient", source = "contact")
    OrderDto OrderToOrderDto(Order order);

    List<OrderDto> OrdersToOrdersDto(List<Order> order);
}
