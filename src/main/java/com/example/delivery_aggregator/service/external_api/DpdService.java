package com.example.delivery_aggregator.service.external_api;

import com.example.delivery_aggregator.dto.aggregator.IndexPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.PackageDto;
import com.example.delivery_aggregator.mappers.DpdMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dpd.ws.calculator._2012_03_20.*;
import ru.dpd.ws.geography._2015_05_20.*;
import ru.dpd.ws.geography._2015_05_20.City;
import ru.dpd.ws.geography._2015_05_20.WSFault_Exception;
import ru.dpd.ws.order2._2012_04_04.*;
import ru.dpd.ws.order2._2012_04_04.Address;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class DpdService {
    @Value("${authorization-codes.dpd.client-number}")
    private Long dpdClientNumber;

    @Value("${authorization-codes.dpd.client-key}")
    private String dpdClientKey;

    private DpdMapper dpdMapper;

    public XMLGregorianCalendar convertStringToXMLGregorianCalendar(String dateStr) throws Exception {
        // Парсим строку в java.util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date = sdf.parse(dateStr);

        // Конвертируем Date -> GregorianCalendar
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        // Конвертируем GregorianCalendar -> XMLGregorianCalendar
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    public Long getCityIdByCityName(String cityName) {
        String countryCode = "RU";
        List<City> response = getCitiesWithCashPay(countryCode);

        return response.stream()
                .filter(city -> cityName.equalsIgnoreCase(city.getCityName()))
                .findFirst()
                .map(ru.dpd.ws.geography._2015_05_20.City::getCityId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Город '%s' не найден в стране '%s'", cityName, countryCode)
                ));
    }

    public List<City> getCitiesWithCashPay(String countryCode) {
        DPDGeography2Service service = new DPDGeography2Service();
        DPDGeography2 port = service.getDPDGeography2Port();

        DpdCitiesCashPayRequest request = new DpdCitiesCashPayRequest();

        ru.dpd.ws.geography._2015_05_20.Auth auth = new ru.dpd.ws.geography._2015_05_20.Auth();
        auth.setClientNumber(dpdClientNumber);
        auth.setClientKey(dpdClientKey);
        request.setAuth(auth);

        request.setCountryCode(countryCode);

        try {
            return port.getCitiesCashPay(request);
        } catch (WSFault_Exception e) {
            throw new RuntimeException("Ошибка сервиса географии DPD: " + e.getFaultInfo().getMessage(), e);
        }
    }

    public ResponseEntity<List<ServiceCost>> getTariffs(IndexPageDataDto indexPageDataDto) {
        try {
            // Создаем экземпляр сервиса
            DPDCalculatorService service = new DPDCalculatorService();

            // Получаем порт для работы с API
            DPDCalculator port = service.getDPDCalculatorPort();

            // Подготавливаем запрос
            ServiceCostRequest request = new ServiceCostRequest();

            // Set auth
            ru.dpd.ws.calculator._2012_03_20.Auth auth = new ru.dpd.ws.calculator._2012_03_20.Auth();
            auth.setClientNumber(dpdClientNumber);
            auth.setClientKey(dpdClientKey);
            request.setAuth(auth);

            // Set pickup location
            CityRequest pickup = new CityRequest();
            pickup.setCityId(getCityIdByCityName(indexPageDataDto.getFromLocation().getCity()));
            request.setPickup(pickup);

            // Set delivery location
            CityRequest delivery = new CityRequest();
            delivery.setCityId(getCityIdByCityName(indexPageDataDto.getToLocation().getCity()));
            request.setDelivery(delivery);

            // Set other parameters
            request.setSelfPickup(indexPageDataDto.getSelfPickup());
            request.setSelfDelivery(indexPageDataDto.getSelfDelivery());
            request.setWeight(indexPageDataDto.getPackages().stream().mapToInt(PackageDto::getWeight).sum());

            // Выполняем запрос
            List<ServiceCost> response = port.getServiceCost2(request);

            // Обрабатываем ответ
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }

    }

    public ResponseEntity<?> createOrder(OrderPageDataDto orderPageDataDto) {
        try {
            // Создаем экземпляр сервиса
            DPDOrderService service = new DPDOrderService();

            // Получаем порт для работы с API
            DPDOrder port = service.getDPDOrderPort();

            // Подготавливаем запрос
            DpdOrdersData request = new DpdOrdersData();

            // Устанавливаем аутентификацию
            ru.dpd.ws.order2._2012_04_04.Auth auth = new ru.dpd.ws.order2._2012_04_04.Auth();
            auth.setClientNumber(dpdClientNumber);
            auth.setClientKey(dpdClientKey);
            request.setAuth(auth);

            // Устанавливаем заголовок заказа
            Header header = new Header();
            XMLGregorianCalendar xmlDate = convertStringToXMLGregorianCalendar(orderPageDataDto.getFromLocation().getDate());
            header.setDatePickup(xmlDate);
            header.setPickupTimePeriod("9-18"); // Стандартный интервал времени

            // Устанавливаем адрес отправителя
            Address senderAddress = new Address();
            senderAddress.setCity(orderPageDataDto.getFromLocation().getCity());
            senderAddress.setStreet(orderPageDataDto.getFromLocation().getStreet());
            senderAddress.setHouse(orderPageDataDto.getFromLocation().getHouse());
            senderAddress.setFlat(orderPageDataDto.getFromLocation().getApartment());
            senderAddress.setIndex(orderPageDataDto.getFromLocation().getPostalCode());
            header.setSenderAddress(senderAddress);

            request.setHeader(header);

            // Устанавливаем параметры заказа
            Order order = new Order();
            order.setOrderNumberInternal(orderPageDataDto.getComment()); // Используем комментарий как внутренний номер
            order.setServiceCode(orderPageDataDto.getTariff().getCode());
            order.setServiceVariant("ДД"); // Вариант доставки "Дверь-Дверь"

            // Устанавливаем параметры груза

            order.setCargoNumPack(orderPageDataDto.getPackages().size());
            order.setCargoWeight(orderPageDataDto.getPackages().stream()
                    .mapToInt(PackageDto::getWeight)
                    .sum());
            order.setCargoVolume(orderPageDataDto.getPackages().stream()
                    .mapToDouble(p -> p.getLength() * p.getWidth() * p.getHeight() / 1_000_000.0)
                    .sum());
            order.setCargoRegistered(false); // По умолчанию не ценный груз

            // Устанавливаем адрес получателя
            Address receiverAddress = new Address();
            receiverAddress.setCity(orderPageDataDto.getToLocation().getCity());
            receiverAddress.setStreet(orderPageDataDto.getToLocation().getStreet());
            receiverAddress.setHouse(orderPageDataDto.getToLocation().getHouse());
            receiverAddress.setFlat(orderPageDataDto.getToLocation().getApartment());
            receiverAddress.setIndex(orderPageDataDto.getToLocation().getPostalCode());
            order.setReceiverAddress(receiverAddress);

            // Устанавливаем контактные данные получателя
/*            Contact receiverContact = new Contact();
            receiverContact.setFio(String.join(" ",
                    orderPageDataDto.getRecipient().getLastName(),
                    orderPageDataDto.getRecipient().getFirstName(),
                    orderPageDataDto.getRecipient().getFatherName()));
            receiverContact.setPhone(orderPageDataDto.getRecipient().getPhone());
            order.setReceiverContact(receiverContact);*/

            // Выполняем запрос
            List<DpdOrderStatus2> response = port.createOrder2(request);

            // Обрабатываем ответ
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

}
