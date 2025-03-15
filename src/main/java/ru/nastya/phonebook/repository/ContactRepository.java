package ru.nastya.phonebook.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastya.phonebook.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
  Optional<Contact> findByName(String name);
}
