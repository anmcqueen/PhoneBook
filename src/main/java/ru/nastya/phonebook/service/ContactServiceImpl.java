 package ru.nastya.phonebook.service;

 import org.springframework.stereotype.Service;
 import ru.nastya.phonebook.exception.ContactNotFoundException;
 import ru.nastya.phonebook.model.Contact;
 import ru.nastya.phonebook.repository.ContactRepository;
 
 @Service
 public class ContactServiceImpl implements ContactService {
   private final ContactRepository contactRepository;
   
   public ContactServiceImpl(ContactRepository contactRepository) {
     this.contactRepository = contactRepository;
   }

   @Override
   public void createContact(Contact contact) {
     contactRepository.save(contact);
   }

   @Override
   public void updateContact(Contact contact) {
     contactRepository.findById(contact.getId()).ifPresent(
             contactFromDb -> {
               contactRepository.delete(contactFromDb);
               contactRepository.save(contact);
         });
   }

   @Override
   public Contact getContact(String name) {
     return contactRepository.findByName(name)
       .orElseThrow(() -> new ContactNotFoundException("Contact is not found: " + name));
   }

   @Override
   public void deleteContact(String name) {
     contactRepository.findByName(name).ifPresent(contactRepository::delete);
   }
 }
