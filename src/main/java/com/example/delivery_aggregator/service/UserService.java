package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.pages.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AggregatorMapper aggregatorMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User createUser(RegistrationPageDataDto registrationPageDataDto){
        User user = aggregatorMapper.registrationPageToUser(registrationPageDataDto);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password("{noop}" + user.getPassword())
                .build();
    }


}
