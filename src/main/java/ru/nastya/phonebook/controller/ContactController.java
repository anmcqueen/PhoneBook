 package ru.nastya.phonebook.controller;
 
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import ru.nastya.phonebook.model.Contact;
 import ru.nastya.phonebook.service.ContactService;
 
 @RestController
 @RequestMapping({"/api/v1/contact/"})
 public class ContactController {
   private final ContactService contactService;
   
   public ContactController(ContactService contactService) {
     this.contactService = contactService;
   }
   
   @PostMapping({"/create"})
   public ResponseEntity<HttpStatus> createContact(@RequestBody Contact contact) {
     contactService.createContact(contact);
     return new ResponseEntity<>(HttpStatus.OK);
   }
   
   @PutMapping({"/update"})
   public ResponseEntity<Contact> updateContact(@RequestBody Contact contact) {
     String name = contact.getName();
     contactService.updateContact(contact);
     return new ResponseEntity<>(HttpStatus.OK);
   }
   
   @GetMapping({"/get/{name}"})
   public ResponseEntity<Contact> getContact(@PathVariable("name") String name) {
     Contact contact = contactService.getContact(name);
     return new ResponseEntity<>(contact, HttpStatus.OK);
   }
   
   @DeleteMapping({"/delete/{name}"})
   public ResponseEntity<HttpStatus> deleteContact(@PathVariable("name") String name) {
     contactService.deleteContact(name);
     return new ResponseEntity<>(HttpStatus.OK);
   }
 }
