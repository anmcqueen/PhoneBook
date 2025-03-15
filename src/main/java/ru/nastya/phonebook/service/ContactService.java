package ru.nastya.phonebook.service;

import ru.nastya.phonebook.model.Contact;

public interface ContactService {
  void createContact(Contact paramContact);
  void updateContact(Contact paramContact);
  Contact getContact(String paramString);
  void deleteContact(String paramString);
}
