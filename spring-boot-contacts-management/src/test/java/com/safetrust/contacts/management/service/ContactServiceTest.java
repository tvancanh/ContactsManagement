/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.service;

import com.safetrust.contacts.management.ExceptionHandler.ResourceNotFoundException;
import com.safetrust.contacts.management.model.Contact;
import com.safetrust.contacts.management.repository.ContactRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * @author tvancanh
 */
public class ContactServiceTest {
    @InjectMocks
    private ContactService service;

    @Mock
    private ContactRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllContacts_notFilter_havePageAndSize_returnContact() {
        //Arrange
        int page = 0;
        int size = 1;
        ResponseEntity<Map<String, Object>> response;
        List<Contact> contacts = new ArrayList<>(
                Arrays.asList(new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM")));
        Page<Contact> contactPage = new PageImpl<Contact>(contacts);
        Pageable pagingSort = PageRequest.of(page, size);
        when(repository.findAll(pagingSort)).thenReturn(contactPage);
        //Act
        response = service.getAllContacts(null, null, page, size);
        //Assert
        Assert.assertEquals(page, response.getBody().get("currentPage"));
        Assert.assertEquals(size, response.getBody().get("totalPages"));
        Assert.assertEquals(1L, response.getBody().get("totalItems"));
    }

    @Test
    public void getAllContacts_havingFilterByFirstNameAndLastName_havePageAndSize_returnContact() {
        //Arrange
        int page = 0;
        int size = 1;
        String firstName = "canh";
        String lastName = "tran";
        ResponseEntity<Map<String, Object>> response;
        List<Contact> contacts = new ArrayList<>(
                Arrays.asList(new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM")));
        Page<Contact> contactPage = new PageImpl<Contact>(contacts);
        Pageable pagingSort = PageRequest.of(page, size);
        when(repository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(firstName, lastName, pagingSort)).thenReturn(contactPage);
        //Act
        response = service.getAllContacts(firstName, lastName, page, size);
        //Assert
        Assert.assertEquals(page, response.getBody().get("currentPage"));
        Assert.assertEquals(size, response.getBody().get("totalPages"));
        Assert.assertEquals(1L, response.getBody().get("totalItems"));
    }

    @Test
    public void getContactById_contactIsExisted_returnContact() {
        //Arrange
        long id = 1;
        ResponseEntity<Contact> response;
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.findById(id)).thenReturn(Optional.of(contact));
        //Act
        response = service.getContactById(id);
        //Assert
        Assert.assertEquals("HCM", response.getBody().getPostalAddress());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getContactById_contactIsNotExisted_throwExceptiont() {
        long id = 1;
        when(repository.findById(id)).thenThrow(new ResourceNotFoundException("ID contact not found"));
        service.getContactById(id);
    }

    @Test
    public void addContact_validInput_responseOk() {
        //Arrange
        ResponseEntity<Contact> response;
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.save(contact)).thenReturn(contact);
        //Act
        response = service.createContact(contact);
        //Assert
        Assert.assertEquals("HCM", response.getBody().getPostalAddress());
    }

    @Test(expected = RuntimeException.class)
    public void addContact_inValidInput_throwException() {
        ResponseEntity<Contact> response;
        Contact contact = new Contact(null, null, "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.save(contact)).thenThrow(new RuntimeException("Bad request"));
        service.createContact(contact);
    }

    @Test(expected = RuntimeException.class)
    public void updateContact_inValidInput_throwException() {
        ResponseEntity<Contact> response;
        Contact contact = new Contact(null, null, "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.save(contact)).thenThrow(new RuntimeException("Bad request"));
        service.createContact(contact);
    }

    @Test
    public void updateContact_validInput_responseOk() {
        //Arrange
        long id = 1;
        ResponseEntity<Contact> response;
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.findById(id)).thenReturn(Optional.of(contact));
        when(repository.save(contact)).thenReturn(contact);
        //Act
        response = service.updateContact(id, contact);
        //Assert
        Assert.assertEquals("HCM", response.getBody().getPostalAddress());
    }

    @Test
    public void deleteContactById_contactRepoCallDeleteByIdOneTime() {
        //Arrange
        long id = 1;
        ResponseEntity<HttpStatus> response;
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        when(repository.findById(id)).thenReturn(Optional.of(contact));
        //Act
        response = service.deleteContact(id);
        //Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void deleteAllContact_contactRepoCallDeleteByIdOneTime() {
        //Act
        service.deleteAllContacts();
        //Assert
        verify(repository, times(1)).deleteAll();
    }
}
