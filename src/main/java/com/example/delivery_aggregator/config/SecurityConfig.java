package com.example.delivery_aggregator.config;
import com.example.delivery_aggregator.service.db.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    private static String loginUrl = "/login";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);

        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
                .requestCache(cache->cache.requestCache(requestCache))
            .formLogin(form -> form
                .loginPage(loginUrl)
                .loginProcessingUrl(loginUrl)
                .successHandler(authenticationSuccessHandler())
            )
            .logout(logout -> logout
                    .logoutSuccessUrl(loginUrl)
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/tariffs/**", "/order", loginUrl, "/registration", "/error").permitAll()
                    .requestMatchers("/js/**", "/css/**").permitAll()
                    .requestMatchers("email/**", "/cities/**", "/user/isAuth").permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            ).build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailService(){
        return userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

}
