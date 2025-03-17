 package ru.nastya.phonebook.service;

 import io.micrometer.core.instrument.Counter;
 import io.micrometer.core.instrument.MeterRegistry;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.stereotype.Service;
 import ru.nastya.phonebook.exception.ContactNotFoundException;
 import ru.nastya.phonebook.model.Contact;
 import ru.nastya.phonebook.repository.ContactRepository;

 import java.util.Optional;

 @Service
 @Slf4j
 public class ContactServiceImpl implements ContactService {
   private final ContactRepository contactRepository;
   private final Counter contactCounter;

   public ContactServiceImpl(ContactRepository contactRepository, MeterRegistry meterRegistry) {
     this.contactRepository = contactRepository;

     // Инициализация счётчика
     this.contactCounter = Counter.builder("contact.counter")
           .description("Количество сохранений контактов")
           .tag("service", "contact")
           .register(meterRegistry);
   }

   @Override
   public void createContact(Contact contact) {
     log.info("Попытка сохранения контакта: {}", contact);
     contactRepository.save(contact);
     contactCounter.increment();
     log.info("Контакт \"{}\" был сохранен в таблицу contact", contact);
   }

   @Override
   public void updateContact(Contact contact) {
     log.info("Попытка обновления контакта: {}", contact);

     contactRepository.findByIdAndName(contact.getId(), contact.getName()).ifPresentOrElse(
       contactFromDb -> {
           log.debug("Найден контакт для обновления: {}", contactFromDb);
           log.info("Изменения в контакте: {} -> {}", contactFromDb, contact);
           // Обновляем поля существующего объекта
           contactFromDb.setName(contact.getName());
           contactFromDb.setNumber(contact.getNumber());
           contactRepository.save(contactFromDb); // Сохраняем обновлённый объект
           log.info("Контакт \"{}\" был успешно обновлён", contactFromDb);
       },
       () -> log.warn("Контакт для обновления не найден: {}", contact)
       );
   }

   @Override
   public Contact getContact(String name) {
       log.info("Поиск контакта по имени: {}", name);

       Optional<Contact> contact = contactRepository.findByName(name);

       if (contact.isPresent()) {
           log.debug("Контакт найден: {}", contact.get());
           return contact.get();
       } else {
           log.warn("Контакт не найден: {}", name);
           throw new ContactNotFoundException("Contact is not found: " + name);
       }
   }

   @Override
   public void deleteContact(String name) {
       log.info("Попытка удаления контакта по имени: {}", name);

       Optional<Contact> contact = contactRepository.findByName(name);

       if (contact.isPresent()) {
           contactRepository.delete(contact.get());
           log.info("Контакт успешно удалён: {}", contact.get());
       } else {
           log.warn("Контакт для удаления не найден: {}", name);
       }
   }
 }
