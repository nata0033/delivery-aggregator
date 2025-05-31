package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.Contact;
import com.example.delivery_aggregator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByPhone(String phone);

    Optional<Contact> findByEmailAndUser(String email, User user);
}
