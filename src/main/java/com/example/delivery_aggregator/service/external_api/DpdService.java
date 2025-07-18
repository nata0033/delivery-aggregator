package com.example.delivery_aggregator.service.external_api;

import com.example.delivery_aggregator.dto.aggregator.IndexPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.mappers.DpdMapper;
import com.example.delivery_aggregator.service.db.DpdCityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dpd.ws.calculator._2012_03_20.*;
import ru.dpd.ws.geography._2015_05_20.*;
import ru.dpd.ws.geography._2015_05_20.City;
import ru.dpd.ws.geography._2015_05_20.WSFault_Exception;
import ru.dpd.ws.order2._2012_04_04.*;
import ru.dpd.ws.tracing._2011_11_18.*;

import java.util.*;

@Service
@AllArgsConstructor
public class DpdService {
    @Value("${authorization-codes.dpd.client-number}")
    private Long dpdClientNumber;

    @Value("${authorization-codes.dpd.client-key}")
    private String dpdClientKey;

    private final DpdMapper dpdMapper;

    private final DpdCityService dpdCityService;

    public List<City> getCitiesWithCashPay(String countryCode) {
        DPDGeography2Service service = new DPDGeography2Service();
        DPDGeography2 port = service.getDPDGeography2Port();

        DpdCitiesCashPayRequest request = dpdMapper.
                dpdAuthDataAndCountryCodeToDpdCitiesCashPayRequest(dpdClientNumber, dpdClientKey, countryCode);

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

            DPDCalculator port = service.getDPDCalculatorPort();

            Long pickupCityId = dpdCityService.getCityIdByCityName(indexPageDataDto.getFromLocation().getCity());
            Long deliveryCityId = dpdCityService.getCityIdByCityName(indexPageDataDto.getToLocation().getCity());
            if(pickupCityId == null ||deliveryCityId == null){
                List<City> cities = getCitiesWithCashPay("RU");

                pickupCityId = cities.stream().filter(city -> city.getCityName().equals(indexPageDataDto.getFromLocation()
                        .getCity())).toList().getFirst().getCityId();
                deliveryCityId = cities.stream().filter(city -> city.getCityName().equals(indexPageDataDto.getToLocation()
                        .getCity())).toList().getFirst().getCityId();

                dpdCityService.saveAll(cities);
            }

            ServiceCostRequest request = dpdMapper.indexPageDataDtoAnddpdAuthDataToServiceCostRequest(indexPageDataDto,
                    dpdClientNumber, dpdClientKey, pickupCityId, deliveryCityId);

            List<ServiceCost> response = port.getServiceCost2(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<DpdOrderStatus2>> createOrder(OrderPageDataDto orderPageDataDto) {
        try {

            DpdOrdersData request = dpdMapper.orderPageDataDtoAndAuthParamsToDpdOrdersData(orderPageDataDto, dpdClientNumber, dpdClientKey);

            DPDOrderService service = new DPDOrderService();
            DPDOrder port = service.getDPDOrderPort();

            List<DpdOrderStatus2> response = port.createOrder2(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<StateParcels> getOrderInfoByClientOrderNr(String clientOrderNr){
        try {

        RequestClientOrder request = dpdMapper.clientOrderNrStringToRequestClientOrder(clientOrderNr, dpdClientNumber, dpdClientKey);

        ParcelTracingService service = new ParcelTracingService();
        ParcelTracing port = service.getParcelTracingPort();

        StateParcels response = port.getStatesByClientOrder(request);

        return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<DpdOrderStatus>> deleteOrderByOrderNumberInternal(String orderNumberInternal){
        try{
            DpdOrderCancellation requeest = dpdMapper.clientOrderNrStringToDpdOrderCancellation(orderNumberInternal, dpdClientNumber, dpdClientKey);

            DPDOrderService service = new DPDOrderService();
            DPDOrder port = service.getDPDOrderPort();

            List<DpdOrderStatus> response = port.cancelOrder(requeest);

            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();

        }
    }
}
