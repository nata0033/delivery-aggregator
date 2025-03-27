package com.example.delivery_aggregator.service;

import com.example.delivery_aggregator.dto.pages.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final AggregatorMapper aggregatorMapper;

    private final UserService userService;

    public Contact create(RegistrationPageDataDto registrationPageDataDto){
        User user = userService.createUser(registrationPageDataDto);
        Contact contact = aggregatorMapper.registrationPageToContact(registrationPageDataDto);
        contact.setUser(user);
        return contactRepository.save(contact);
    }
}
