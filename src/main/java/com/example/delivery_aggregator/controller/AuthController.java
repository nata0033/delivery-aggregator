package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Data
@RequiredArgsConstructor
public class AuthController {

    private final ContactService contactService;
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Principal principal, Model model){
        String userLogin = (principal != null) ? principal.getName() : "anonymous";
        model.addAttribute("userLogin", userLogin);
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute RegistrationPageDataDto registrationPageDto, HttpServletRequest request, Model model){
        Contact contact = contactService.create(registrationPageDto);

        UserDetails user = userService.loadUserByUsername(contact.getUser().getLogin());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                registrationPageDto.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return "redirect:/account";
    }
}
