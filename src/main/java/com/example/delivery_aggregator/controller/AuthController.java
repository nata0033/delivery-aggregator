package com.example.delivery_aggregator.controller;

import com.example.delivery_aggregator.dto.aggregator.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.service.db.ContactService;
import com.example.delivery_aggregator.service.db.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@Data
@RequiredArgsConstructor
public class AuthController {

    private final ContactService contactService;
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(){
        return "registration";
    }

    @PostMapping("/registration")
    public ResponseEntity<User> createUser(@RequestBody RegistrationPageDataDto registrationPageDto, HttpServletRequest request){
        try {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(contact.getUser());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/isAuth")
    public ResponseEntity<Boolean> checkUserIsAuth(Principal principal) {
        boolean isAuthenticated = principal != null;
        return ResponseEntity.ok(isAuthenticated);
    }
}
