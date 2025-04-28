package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.aggregator.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.aggregator.IndexPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.TariffDto;
import com.example.delivery_aggregator.dto.aggregator.TariffsPageData;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.mappers.DpdMapper;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Data
@RequiredArgsConstructor
public class TariffController {

    private final AggregatorMapper aggregatorMapper;

    private final CdekService cdekService;
    private final CdekMapper cdekMapper;

    private final DpdService dpdService;
    private final DpdMapper dpdMapper;

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageDataDto indexPageDataDto, Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        ResponseEntity<CdekCalculatorResponseDto> cdekResponseEntity = cdekService.getTariffs(indexPageDataDto);
        ResponseEntity<?> dpdResponseEntity = dpdService.getTariffs(indexPageDataDto);

        TariffsPageData tariffsPageData = aggregatorMapper.indexPageDataDtoToTariffsPageData(indexPageDataDto);
        DeliveryServiceDto cdekDeliveryServiceDto = new DeliveryServiceDto("CDEK", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        DeliveryServiceDto dpdDeliveryServiceDto = new DeliveryServiceDto("DPD", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/DPD_logo_%282015%29.svg/177px-DPD_logo_%282015%29.svg.png");

        tariffsPageData.setTariffs(
                Stream.concat(
                        cdekMapper.cdekCalculatorResponseDtoToTariffDtoList(cdekResponseEntity.getBody(), cdekDeliveryServiceDto).stream(),
                        dpdMapper.serviceCostListToTariffDtoList((List<ServiceCost>) dpdResponseEntity.getBody(), dpdDeliveryServiceDto).stream()
                ).collect(Collectors.toList())
        );

        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().sorted(Comparator.comparing(TariffDto::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }
}
