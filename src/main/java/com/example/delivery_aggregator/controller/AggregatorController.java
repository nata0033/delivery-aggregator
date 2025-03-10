package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.entity.response.DeliveryParams;
import com.example.delivery_aggregator.service.CdekService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@Data
public class AggregatorController {

    @Autowired
    private final CdekService cdekService;

    @GetMapping()
    public String index(){
        return "index";
    }

//    @PostMapping()
//    public ResponseEntity<String> findTariffs(@RequestParam("from_location") String fromLocation,
//                                            @RequestParam("to_location") String toLocation,
//                                            @RequestParam("weight") List<Integer> weight,
//                                            @RequestParam("length") List<Integer> length,
//                                            @RequestParam("width") List<Integer> width,
//                                            @RequestParam("height") List<Integer> height){
//        Integer fromLocationCode = cdekService.getCitiesCode(fromLocation);
//        Integer toLocationCode = cdekService.getCitiesCode(toLocation);
//        return cdekService.tariffList(fromLocationCode, toLocationCode, weight, length, width, height);
//    }

    @PostMapping()
    public String findTariffs(@ModelAttribute DeliveryParams deliveryParams, Model model) {
        System.out.println(deliveryParams);
        return "index";
    }

    @GetMapping("/test")
    public String test(Model model){
        return "test";
    }

}
