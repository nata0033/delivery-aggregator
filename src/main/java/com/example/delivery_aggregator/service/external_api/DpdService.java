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

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
        // Используем формат "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

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

    public ResponseEntity<List<DpdOrderStatus2>> createOrder(OrderPageDataDto orderPageDataDto) {
        try {

            DpdOrdersData request = dpdMapper.OrderPageDataDtoAndAuthParamsToDpdOrdersData(orderPageDataDto, dpdClientNumber, dpdClientKey);

            DPDOrderService service = new DPDOrderService();
            DPDOrder port = service.getDPDOrderPort();

            List<DpdOrderStatus2> response = port.createOrder2(request);

            // Обрабатываем ответ
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }
}
