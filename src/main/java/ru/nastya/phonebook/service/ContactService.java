package ru.nastya.phonebook.service;

import ru.nastya.phonebook.model.Contact;

public interface ContactService {
  void createContact(Contact contact);
  void updateContact(Contact contact);
  Contact getContact(String name);
  void deleteContact(String name);
}
