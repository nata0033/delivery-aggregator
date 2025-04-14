package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.external_api.cdek.calculator.CdekCalculatorResponseDto;
import com.example.delivery_aggregator.dto.pages.DeliveryServiceDto;
import com.example.delivery_aggregator.dto.pages.IndexPageDataDto;
import com.example.delivery_aggregator.dto.pages.TariffDto;
import com.example.delivery_aggregator.dto.pages.TariffsPageData;
import com.example.delivery_aggregator.mappers.CdekMapper;
import com.example.delivery_aggregator.service.external_api.CdekService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.Objects;

@Controller
@Data
@RequiredArgsConstructor
public class TariffController {

    private final CdekService cdekService;
    private final CdekMapper cdekMapper;

    @PostMapping("/tariffs")
    public String getTariffs(@ModelAttribute IndexPageDataDto indexPageDataDto, Principal principal, Model model) {
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);

        ResponseEntity<?> responseEntity = cdekService.getTariffs(indexPageDataDto);

        TariffsPageData tariffsPageData = cdekMapper.cdekCalculatorResponseToTariffsPageData((CdekCalculatorResponseDto) responseEntity.getBody(), indexPageDataDto);
        tariffsPageData.setService(new DeliveryServiceDto());
        tariffsPageData.getService().setName("CDEK");
        tariffsPageData.getService().setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/CDEK_logo.svg/145px-CDEK_logo.svg.png");
        tariffsPageData.setTariffs(tariffsPageData.getTariffs().stream().filter(t-> Objects.equals(t.getName(), "Посылка дверь-дверь")).sorted(Comparator.comparing(TariffDto::getPrice)).toList());

        model.addAttribute(tariffsPageData);
        return "tariffs";
    }
}
