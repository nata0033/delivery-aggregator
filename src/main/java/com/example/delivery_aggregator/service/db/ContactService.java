package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.pages.OrderPageDataDto;
import com.example.delivery_aggregator.dto.pages.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final AggregatorMapper aggregatorMapper;

    private final UserService userService;

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact findById(UUID id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
    }

    public Contact findByEmail(String email){
        return contactRepository.findByEmail(email).orElseThrow();
    }

    public Contact findByPhone(String phone){return contactRepository.findByPhone(phone).orElseThrow();}

    public void create(RegistrationPageDataDto registrationPageDataDto){
        User user = userService.createUser(registrationPageDataDto);
        Contact contact = aggregatorMapper.registrationPageToContact(registrationPageDataDto);
        contact.setUser(user);
        contactRepository.save(contact);
    }

    public Contact create(OrderPageDataDto orderPageDataDto, User user){
        Contact contact = findByPhone(orderPageDataDto.getRecipient().getPhone());
        if(contact != null){
            return contact;
        }

        Contact newContact = aggregatorMapper.contactDtoToContact(orderPageDataDto.getRecipient());
        newContact.setUser(user);
        return contactRepository.save(newContact);
    }
}
