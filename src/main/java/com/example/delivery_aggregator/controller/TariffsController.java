package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.aggregator.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.aggregator.IndexPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.TariffDto;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.mappers.DpdMapper;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dpd.ws.calculator._2012_03_20.ServiceCost;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Controller
@RequestMapping("/tariffs")
@RequiredArgsConstructor
public class TariffsController {

    private final AggregatorMapper aggregatorMapper;

    private final CdekService cdekService;
    private final CdekMapper cdekMapper;

    private final DpdService dpdService;
    private final DpdMapper dpdMapper;

    @PostMapping
    public String createTariffsPage(Principal principal, Model model){
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "tariffs";
    }

    @PostMapping("/get")
    public ResponseEntity<List<TariffDto>> getTariffsAjax(@RequestBody IndexPageDataDto indexPageDataDto) {
        try {
            ResponseEntity<CdekCalculatorResponseDto> cdekResponse = cdekService.getTariffs(indexPageDataDto);
            ResponseEntity<List<ServiceCost>> dpdResponse = dpdService.getTariffs(indexPageDataDto);

            DeliveryServiceDto cdekDeliveryService = new DeliveryServiceDto("CDEK", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");

            DeliveryServiceDto dpdDeliveryService = new DeliveryServiceDto("DPD", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/DPD_logo_%282015%29.svg/177px-DPD_logo_%282015%29.svg.png");

            List<TariffDto> cdekTariffs = cdekMapper.cdekCalculatorResponseDtoToTariffDtoList(
                    cdekResponse.getBody(),
                    cdekDeliveryService
            );

            List<TariffDto> dpdTariffs = dpdMapper.serviceCostListToTariffDtoList(dpdResponse.getBody(), dpdDeliveryService);

            List<TariffDto> allTariffs = Stream.concat(cdekTariffs.stream(), dpdTariffs.stream())
                    .sorted(Comparator.comparing(TariffDto::getPrice))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(allTariffs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
