package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.aggregator.ContactDto;
import com.example.delivery_aggregator.dto.aggregator.RegistrationPageDataDto;
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

    private final UserService userService;

    private final AggregatorMapper aggregatorMapper;

    public Contact findByEmail(String email){
        return contactRepository.findByEmail(email).orElse(null);
    }

    public Contact findUserContact(ContactDto contactDto, User user){
        return contactRepository.findByEmailAndUser(contactDto.getEmail(), user).orElseGet(() -> create(contactDto, user));
    }

    public Contact create(ContactDto contactDto, User user){
        Contact contact = aggregatorMapper.contactDtoToContact(contactDto);
        contact.setUser(user);
        return contactRepository.save(contact);
    }

    public Contact create(RegistrationPageDataDto registrationPageDataDto){
        User user = userService.createUser(registrationPageDataDto);
        Contact contact = aggregatorMapper.registrationPageToContact(registrationPageDataDto);
        contact.setUser(user);
        return contactRepository.save(contact);
    }
}
