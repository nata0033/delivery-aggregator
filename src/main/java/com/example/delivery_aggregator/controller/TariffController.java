package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.pages.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pages.TariffDto;
import com.example.delivery_aggregator.dto.pages.TariffsPageData;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.mappers.DpdMapper;
import com.example.delivery_aggregator.mappers.YandexMapper;
import com.example.delivery_aggregator.service.external_api.CdekService;
import com.example.delivery_aggregator.service.external_api.DpdService;
import com.example.delivery_aggregator.service.external_api.YandexService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.Objects;

@Controller
@Data
@RequiredArgsConstructor
public class TariffController {

    private final CdekService cdekService;
    private final CdekMapper cdekMapper;

    private final YandexService yandexService;
    private final YandexMapper yandexMapper;

    private final DpdService dpdService;
    private final DpdMapper dpdMapper;

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageDataDto indexPageDataDto, Principal principal, Model model) throws IOException {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        ResponseEntity<?> cdekResponseEntity = cdekService.getTariffs(indexPageDataDto);
        //ResponseEntity<?> yandexResponseEntity = yandexService.getTariffs(indexPageDataDto);
        ResponseEntity<?> dpdResponseEntity = dpdService.getTariffs(indexPageDataDto);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponseDto) cdekResponseEntity.getBody(), indexPageDataDto);

        tariffsPageData.setService(new DeliveryServiceDto());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(TariffDto::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }
}
