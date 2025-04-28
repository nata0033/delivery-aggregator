package com.example.delivery_aggregator.service.db;

import com.example.delivery_aggregator.dto.db.ContactDto;
import com.example.delivery_aggregator.dto.aggregator.OrderPageDataDto;
import com.example.delivery_aggregator.dto.aggregator.RegistrationPageDataDto;
import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import com.example.delivery_aggregator.mappers.AggregatorMapper;
import com.example.delivery_aggregator.repository.ContactRepository;
import com.example.delivery_aggregator.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final AggregatorMapper aggregatorMapper;

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact findById(UUID id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
    }

    public Contact findByEmail(String email){
        return contactRepository.findByEmail(email).orElse(null);
    }

    public Contact findByPhone(String phone){return contactRepository.findByPhone(phone).orElse(null);}

    public Contact create(RegistrationPageDataDto registrationPageDataDto){
        User user = userService.createUser(registrationPageDataDto);
        Contact contact = aggregatorMapper.registrationPageToContact(registrationPageDataDto);
        contact.setUser(user);
        return contactRepository.save(contact);
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

    public Contact update(ContactDto contactDto, MultipartFile avatar){
        Contact contact = contactRepository.findById(contactDto.getId()).orElse(null);

        if(!Objects.equals(contactDto.getEmail(), contact.getEmail())){
            User user = userRepository.findByLogin(contact.getEmail()).orElse(null);
            user.setLogin(contactDto.getEmail());
            userRepository.save(user);
        }

        assert contact != null;
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setFatherName(contactDto.getFatherName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhone(contactDto.getPhone());

        return contactRepository.save(contact);
    }
}
