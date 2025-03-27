package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.pages.*;
import com.example.delivery_aggregator.entity.Address;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AggregatorMapper {

    OrderPageDataDto deliveryDataToOrderPageData(DeliveryDataDto deliveryData);

    @Mapping(target = "login", source = "email")
    User loginPageDataToUser(LoginPageDataDto loginPageData);

    Contact registrationPageToContact(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "login", source = "email")
    User registrationPageToUser(RegistrationPageDataDto registrationPageDto);

    @Mapping(target = "email", source = "login")
    ContactDto userToContact(User user);

    ContactDto contactToContactDto(Contact contact);

    LocationDto addressToLocation(Address address);

    OrderDto orderToOrderDto(Order order);
}
