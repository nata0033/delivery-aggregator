package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.aggregator.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AggregatorMapper aggregatorMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElse(null);
        assert user != null;
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password("{noop}" + user.getPassword())
                .build();
    }

    public User findByLogin(String login){
        return userRepository.findByLogin(login).orElse(null);
    }

    public User createUser(RegistrationPageDataDto registrationPageDataDto){
        User newUser = aggregatorMapper.registrationPageToUser(registrationPageDataDto);
        User oldUser = findByLogin(newUser.getLogin());
        if(oldUser == null) {
            return userRepository.save(newUser);
        }
        return  oldUser;
    }
}
