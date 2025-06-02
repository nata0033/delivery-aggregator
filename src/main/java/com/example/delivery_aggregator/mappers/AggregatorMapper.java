package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.aggregator.ContactDto;
import com.example.delivery_aggregator.dto.aggregator.*;
import com.example.delivery_aggregator.dto.cdek.order.CdekOrderResponseDto;
import com.example.delivery_aggregator.entity.*;
import com.example.delivery_aggregator.entity.Package;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dpd.ws.order2._2012_04_04.DpdOrderStatus2;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AggregatorMapper {
    Contact registrationPageToContact(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "login", source = "email")
    User registrationPageToUser(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "userData", source = "contact")
    @Mapping(target = "addresses", source = "contact.addresses")
    @Mapping(target = "sendOrders", source = "user.sentOrders")
    @Mapping(target = "receivedOrders", source = "contact.receivedOrders")
    @Mapping(target = "contacts", source = "user.contacts")
    AccountPageDataDto contactToAccountPageDataDto(User user, Contact contact);

    Contact contactDtoToContact(ContactDto contactDto);

    //Заказ
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceOrderNumber", source = "cdekOrderResponseDto.entity.uuid")
    @Mapping(target = "price", source = "orderPageDataDto.tariff.price")
    @Mapping(target = "fromLocation", source = "orderPageDataDto.fromLocation")
    @Mapping(target = "toLocation", source = "orderPageDataDto.toLocation")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "packages", source = "orderPageDataDto.packages")
    Order orderPageDataDtoAndCdekOrderResponseDto(OrderPageDataDto orderPageDataDto, User user, Contact contact,
                                                  DeliveryService deliveryService, CdekOrderResponseDto cdekOrderResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceOrderNumber", source = "dpdOrderResponseDto.orderNumberInternal")
    @Mapping(target = "price", source = "orderPageDataDto.tariff.price")
    @Mapping(target = "fromLocation", source = "orderPageDataDto.fromLocation")
    @Mapping(target = "toLocation", source = "orderPageDataDto.toLocation")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "packages", source = "orderPageDataDto.packages")
    Order orderPageDataDtoAndCdekOrderResponseDto(OrderPageDataDto orderPageDataDto, User user, Contact contact,
                                                  DeliveryService deliveryService, DpdOrderStatus2 dpdOrderResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "length", source = "packageDto.length")
    @Mapping(target = "height", source = "packageDto.height")
    @Mapping(target = "width", source = "packageDto.width")
    @Mapping(target = "weight", source = "packageDto.weight")
    @Mapping(target = "order", source = "order")
    Package packageDtoToPackage(PackageDto packageDto, Order order);

    default List<Package> packageDtoListToPackageList(List<PackageDto> packageDtoList, Order order){
        return packageDtoList.stream().map(p->packageDtoToPackage(p, order)).toList();
    }

    default String locationDtoToLocationString(LocationDto locationDto) {
        return locationDto.getCity() + " " + locationDto.getStreet() + " " + locationDto.getHouse() + " " + locationDto.getApartment();
    }

    //Аккаунт
    ContactDto contactToContactDto(Contact contact);

    OrderDto orderToOrderDto(Order order);

    List<OrderDto> orderListToOrderDtoList(List<Order> orders);
}
