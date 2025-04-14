package com.example.delivery_aggregator.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Data
@RequiredArgsConstructor
public class AggregatorController {

    private final VerificationController verificationController;

    @GetMapping("/test")
    public String test(){
        return "index";
    }

    @GetMapping()
    public String index(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "index";
    }
}