package com.example.delivery_aggregator.mappers;

import com.example.delivery_aggregator.dto.db.AddressDto;
import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.db.OrderDto;
import com.example.delivery_aggregator.dto.pages.AccountPageDataDto;
import com.example.delivery_aggregator.dto.pages.DeliveryDataDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pages.OrderPageDataDto;
import com.example.delivery_aggregator.dto.pages.PackageDto;
import com.example.delivery_aggregator.dto.pages.RegistrationPageDataDto;
import com.example.delivery_aggregator.dto.pages.TariffDto;
import com.example.delivery_aggregator.dto.pages.TariffsPageData;
import com.example.delivery_aggregator.entity.Address;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.DeliveryService;
import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.Package;
import com.example.delivery_aggregator.entity.User;
import java.time.format.DateTimeFormatter;
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
public class AggregatorMapperImpl implements AggregatorMapper {

    @Override
    public OrderPageDataDto deliveryDataToOrderPageData(DeliveryDataDto deliveryData, Contact contact) {
        if ( deliveryData == null && contact == null ) {
            return null;
        }

        OrderPageDataDto orderPageDataDto = new OrderPageDataDto();

        if ( deliveryData != null ) {
            orderPageDataDto.setFromLocation( deliveryData.getFromLocation() );
            orderPageDataDto.setToLocation( deliveryData.getToLocation() );
            orderPageDataDto.setTariff( deliveryData.getTariff() );
        }
        orderPageDataDto.setSender( contactToContactDto( contact ) );

        return orderPageDataDto;
    }

    @Override
    public Contact registrationPageToContact(RegistrationPageDataDto registrationPageDto) {
        if ( registrationPageDto == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setFirstName( registrationPageDto.getFirstName() );
        contact.setLastName( registrationPageDto.getLastName() );
        contact.setFatherName( registrationPageDto.getFatherName() );
        contact.setEmail( registrationPageDto.getEmail() );
        contact.setPhone( registrationPageDto.getPhone() );

        return contact;
    }

    @Override
    public User registrationPageToUser(RegistrationPageDataDto registrationPageDto) {
        if ( registrationPageDto == null ) {
            return null;
        }

        User user = new User();

        user.setLogin( registrationPageDto.getEmail() );
        user.setPassword( registrationPageDto.getPassword() );

        return user;
    }

    @Override
    public AccountPageDataDto contactToAccountPageDataDto(User user, Contact contact) {
        if ( user == null && contact == null ) {
            return null;
        }

        AccountPageDataDto accountPageDataDto = new AccountPageDataDto();

        if ( user != null ) {
            accountPageDataDto.setSendOrders( OrdersToOrdersDto( user.getSentOrders() ) );
            accountPageDataDto.setContacts( contactListToContactDtoList( user.getContacts() ) );
        }
        if ( contact != null ) {
            accountPageDataDto.setUserData( contactToContactDto( contact ) );
            accountPageDataDto.setAddresses( addressListToAddressDtoList( contact.getAddresses() ) );
            accountPageDataDto.setReceivedOrders( OrdersToOrdersDto( contact.getReceivedOrders() ) );
        }

        return accountPageDataDto;
    }

    @Override
    public Contact contactDtoToContact(ContactDto contactDto) {
        if ( contactDto == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setId( contactDto.getId() );
        contact.setFirstName( contactDto.getFirstName() );
        contact.setLastName( contactDto.getLastName() );
        contact.setFatherName( contactDto.getFatherName() );
        contact.setEmail( contactDto.getEmail() );
        contact.setPic( contactDto.getPic() );
        contact.setPhone( contactDto.getPhone() );

        return contact;
    }

    @Override
    public Order orderPageDataDtoToOrder(OrderPageDataDto orderPageDataDto, String serviceOrderNumber) {
        if ( orderPageDataDto == null && serviceOrderNumber == null ) {
            return null;
        }

        Order order = new Order();

        if ( orderPageDataDto != null ) {
            Integer price = orderPageDataDtoTariffPrice( orderPageDataDto );
            if ( price != null ) {
                order.setPrice( price.floatValue() );
            }
            order.setFromLocation( locationDtoToLocationString( orderPageDataDto.getFromLocation() ) );
            order.setToLocation( locationDtoToLocationString( orderPageDataDto.getToLocation() ) );
            order.setContact( contactDtoToContact( orderPageDataDto.getRecipient() ) );
        }
        order.setServiceOrderNumber( serviceOrderNumber );

        return order;
    }

    @Override
    public Package packageDtoToPackage(PackageDto packageDto) {
        if ( packageDto == null ) {
            return null;
        }

        Package package1 = new Package();

        package1.setWeight( packageDto.getWeight() );
        package1.setLength( packageDto.getLength() );
        package1.setWidth( packageDto.getWidth() );
        package1.setHeight( packageDto.getHeight() );

        return package1;
    }

    @Override
    public OrderDto OrderToOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setServiceName( orderDeliveryServiceName( order ) );
        orderDto.setNumber( order.getServiceOrderNumber() );
        if ( order.getCreatedAt() != null ) {
            orderDto.setDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( order.getCreatedAt() ) );
        }
        orderDto.setRecipient( contactToContactDto( order.getContact() ) );
        orderDto.setFromLocation( order.getFromLocation() );
        orderDto.setToLocation( order.getToLocation() );
        orderDto.setId( order.getId() );
        orderDto.setStatus( order.getStatus() );
        if ( order.getPrice() != null ) {
            orderDto.setPrice( order.getPrice().intValue() );
        }
        orderDto.setPackages( packageListToPackageDtoList( order.getPackages() ) );

        return orderDto;
    }

    @Override
    public List<OrderDto> OrdersToOrdersDto(List<Order> order) {
        if ( order == null ) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>( order.size() );
        for ( Order order1 : order ) {
            list.add( OrderToOrderDto( order1 ) );
        }

        return list;
    }

    @Override
    public TariffsPageData indexPageDataDtoToTariffsPageData(IndexPageDataDto indexPageDataDto) {
        if ( indexPageDataDto == null ) {
            return null;
        }

        TariffsPageData tariffsPageData = new TariffsPageData();

        tariffsPageData.setFromLocation( indexPageDataDto.getFromLocation() );
        tariffsPageData.setToLocation( indexPageDataDto.getToLocation() );

        tariffsPageData.setPackageQuantity( (int) indexPageDataDto.getPackages().stream().count() );
        tariffsPageData.setPackageWeight( indexPageDataDto.getPackages().stream().mapToInt(p -> p.getWeight()).sum() );

        return tariffsPageData;
    }

    protected ContactDto contactToContactDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setId( contact.getId() );
        contactDto.setFirstName( contact.getFirstName() );
        contactDto.setLastName( contact.getLastName() );
        contactDto.setFatherName( contact.getFatherName() );
        contactDto.setEmail( contact.getEmail() );
        contactDto.setPhone( contact.getPhone() );
        contactDto.setPic( contact.getPic() );

        return contactDto;
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setId( address.getId() );
        addressDto.setCountry( address.getCountry() );
        addressDto.setState( address.getState() );
        addressDto.setCity( address.getCity() );
        addressDto.setHouse( address.getHouse() );
        addressDto.setApartment( address.getApartment() );
        addressDto.setPostalCode( address.getPostalCode() );

        return addressDto;
    }

    protected List<AddressDto> addressListToAddressDtoList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressDto> list1 = new ArrayList<AddressDto>( list.size() );
        for ( Address address : list ) {
            list1.add( addressToAddressDto( address ) );
        }

        return list1;
    }

    protected List<ContactDto> contactListToContactDtoList(List<Contact> list) {
        if ( list == null ) {
            return null;
        }

        List<ContactDto> list1 = new ArrayList<ContactDto>( list.size() );
        for ( Contact contact : list ) {
            list1.add( contactToContactDto( contact ) );
        }

        return list1;
    }

    private Integer orderPageDataDtoTariffPrice(OrderPageDataDto orderPageDataDto) {
        TariffDto tariff = orderPageDataDto.getTariff();
        if ( tariff == null ) {
            return null;
        }
        return tariff.getPrice();
    }

    private String orderDeliveryServiceName(Order order) {
        DeliveryService deliveryService = order.getDeliveryService();
        if ( deliveryService == null ) {
            return null;
        }
        return deliveryService.getName();
    }

    protected PackageDto packageToPackageDto(Package package1) {
        if ( package1 == null ) {
            return null;
        }

        PackageDto packageDto = new PackageDto();

        packageDto.setWeight( package1.getWeight() );
        packageDto.setLength( package1.getLength() );
        packageDto.setWidth( package1.getWidth() );
        packageDto.setHeight( package1.getHeight() );

        return packageDto;
    }

    protected List<PackageDto> packageListToPackageDtoList(List<Package> list) {
        if ( list == null ) {
            return null;
        }

        List<PackageDto> list1 = new ArrayList<PackageDto>( list.size() );
        for ( Package package1 : list ) {
            list1.add( packageToPackageDto( package1 ) );
        }

        return list1;
    }
}
