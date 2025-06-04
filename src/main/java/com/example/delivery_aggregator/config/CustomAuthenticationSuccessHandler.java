package com.example.delivery_aggregator.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String deliveryData = Optional.ofNullable(request.getCookies())
                .map(Arrays::stream)
                .flatMap(stream -> stream.filter(c -> "delivery_data".equals(c.getName())).findFirst())
                .map(Cookie::getValue)
                .orElse(null);

        if (deliveryData == null || deliveryData.isEmpty()) {
            getRedirectStrategy().sendRedirect(request, response, "/account");
        }

        response.sendRedirect("/order?retryOrder=true");
    }
}
