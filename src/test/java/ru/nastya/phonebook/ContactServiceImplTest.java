package ru.nastya.phonebook;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.nastya.phonebook.exception.ContactNotFoundException;
import ru.nastya.phonebook.model.Contact;
import ru.nastya.phonebook.repository.ContactRepository;
import ru.nastya.phonebook.service.ContactServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

public class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepositoryMock;

//    @InjectMocks
//    private ContactServiceImpl contactService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void createContactTest() {
//        ContactRepository contactRepositoryMock = Mockito.mock(ContactRepository.class); // @Mock
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks

        // Подготовка данных
        Contact contact = new Contact(1L, "Tomas", "+79454321234");

        // Мокируем поведение репозитория
        Mockito.when(contactRepositoryMock.save(contact)).thenReturn(contact);

        // Вызов метода
        contactService.createContact(contact);

        // Проверка, что метод save был вызван
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).save(contact);
    }


    @Test
    public void updateContactTest() {
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks
        // Подготовка данных
        Contact contactFromDb = new Contact(1L, "Tomas", "+79454321234");
        Contact contact = new Contact(1L, "Toma", "+794543219876");

        Mockito.when(contactRepositoryMock.findByIdAndName(1L, "Tomas")).thenReturn(Optional.of(contactFromDb));
        Mockito.when(contactRepositoryMock.save(contactFromDb)).thenReturn(contactFromDb); // непонятно, должны изменить поля у contactFromDb и сохранить contactFromDb

        // Вызов метода
        contactService.updateContact(contact);

        // Проверка, что поля обновились
        assertEquals("Toma", contactFromDb.getName()); // не работает, т.к. поля не меняются
        assertEquals("+794543219876", contactFromDb.getNumber()); // не работает, т.к. поля не меняются

        // Проверка, что методы были вызваны
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findByIdAndName(1L, "Tomas");
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).save(contact);


    }

    @Test
    public void getContactTest() {
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks

        Contact contact = new Contact(1L, "Tomas", "+79454321234");

        Mockito.when(contactRepositoryMock.findByName("Tomas")).thenReturn(Optional.of(contact));

        // Вызов метода
        Contact foundContact = contactService.getContact("Tomas");

        // Проверка результата
        assertNotNull(foundContact);
        assertEquals("Tomas", foundContact.getName());
        assertEquals("+79454321234", foundContact.getNumber());

        // Проверка, что метод findByName был вызван
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findByName("Tomas");
    }

    @Test
    public void testGetContactNotFound() {
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks
        // Мокируем поведение репозитория
        Mockito.when(contactRepositoryMock.findByName("Unknown")).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(ContactNotFoundException.class, () -> contactService.getContact("Unknown"));

        // Проверка, что метод findByName был вызван
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findByName("Unknown");
    }

    @Test
    public void testDeleteContact() {
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks

        Contact contact = new Contact(1L, "Tomas", "+79454321234");

        // Мокируем поведение репозитория
        Mockito.when(contactRepositoryMock.findByName("Tomas")).thenReturn(Optional.of(contact));
        doNothing().when(contactRepositoryMock).delete(contact);

        // Вызов метода
        contactService.deleteContact("Tomas");

        // Проверка, что методы были вызваны
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findByName("Tomas");
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).delete(contact);
    }

    @Test
    public void testDeleteContactNotFound() {
        ContactServiceImpl contactService = new ContactServiceImpl(contactRepositoryMock, Mockito.mock(MeterRegistry.class)); // @InjectMocks
        // Мокируем поведение репозитория
        Mockito.when(contactRepositoryMock.findByName("Unknown")).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(ContactNotFoundException.class, () -> contactService.deleteContact("Unknown"));

        // Проверка, что метод findByName был вызван
        Mockito.verify(contactRepositoryMock, Mockito.times(1)).findByName("Unknown");
        Mockito.verify(contactRepositoryMock, Mockito.never()).delete(any());
    }

}

/*
Создать объект класса ContactServiceImpl через new.
Полностью протестировать класс ContactServiceImpl.
@BeforeEach, @BeforeAll - почитать и использовать
Mockito.verify()
assert


не работает @InjectMocks
не работает createContactTest() из-за метрик
не работает updateContactTest(), т.к. непонятно, как выполнить условие
почему нужен Optional?


 */

