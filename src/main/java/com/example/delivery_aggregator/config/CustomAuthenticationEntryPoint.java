package com.example.delivery_aggregator.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (request.getRequestURI().equals("/order/create")) {
            request.getSession().setAttribute("SAVED_ORDER_DATA",
                    request.getParameterMap());
        }
        response.sendRedirect("/login");
    }
}
