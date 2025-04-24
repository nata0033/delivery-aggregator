package com.example.delivery_aggregator.service.external_api;

import com.example.delivery_aggregator.dto.external_api.dpd.DpdAuthDto;
import com.example.delivery_aggregator.dto.external_api.dpd.DpdServiceCostRequestDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.mappers.DpdMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.IOException;

@Service
@AllArgsConstructor
public class DpdService {
    private static final String SOAP_ACTION = "http://dpd.ru/ws/calculator/2012/03/21/getServiceCost2";

    @Value("${authorization-codes.dpd.client-number}")
    private Long dpdClientNumber;

    @Value("${authorization-codes.dpd.client-key}")
    private String dpdClientKey;

    private DpdMapper dpdMapper;

//    private final WebServiceTemplate webServiceTemplate;
//
//    @Autowired
//    public DpdService(@Qualifier("dpdWebServiceTemplate") WebServiceTemplate webServiceTemplate) {
//        this.webServiceTemplate = webServiceTemplate;
//    }


    public ResponseEntity<?> getTariffs(IndexPageDataDto indexPageDataDto) {
        DpdServiceCostRequestDto dpdServiceCostRequestDto = dpdMapper.indexPageDataDtoToDpdServiceCostRequestDto(indexPageDataDto);

        DpdAuthDto dpdAuthDto = new DpdAuthDto();
        dpdAuthDto.setClientNumber(dpdClientNumber);
        dpdAuthDto.setClientKey(dpdClientKey);

        dpdServiceCostRequestDto.setAuth(dpdAuthDto);
        dpdServiceCostRequestDto.setSelfPickup(true);
        dpdServiceCostRequestDto.setSelfDelivery(true);

        return null;
    }
}
