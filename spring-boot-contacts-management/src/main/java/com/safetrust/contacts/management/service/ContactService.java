/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.service;

import com.safetrust.contacts.management.ExceptionHandler.ResourceNotFoundException;
import com.safetrust.contacts.management.model.Contact;
import com.safetrust.contacts.management.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    private boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public ResponseEntity<Map<String, Object>> getAllContacts(String firstName, String lastName, int page, int size) {
        List<Contact> contacts = new ArrayList<Contact>();
        Pageable pagingSort = PageRequest.of(page, size);

        Page<Contact> contactPage;
        if (isEmpty(firstName) && isEmpty(lastName)) {
            contactPage = contactRepository.findAll(pagingSort);
        } else {
            contactPage = contactRepository
                    .findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(firstName, lastName, pagingSort);
        }
        contacts = contactPage.getContent();

        if (contacts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("name", contacts);
        response.put("currentPage", contactPage.getNumber());
        response.put("totalItems", contactPage.getTotalElements());
        response.put("totalPages", contactPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Contact> getContactById(long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id contact not found: " + id));
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    public ResponseEntity<Contact> createContact(Contact contact) {
        Contact _contact = contactRepository.save(new Contact(contact.getLastName(), contact.getFirstName(),
                contact.getEmail(), contact.getPhone(), contact.getPostalAddress()));
        return new ResponseEntity<>(_contact, HttpStatus.OK);
    }

    public ResponseEntity<Contact> updateContact(long id, Contact contact) {
        Contact _contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id contact not found: " + id));
        _contact.setFirstName(contact.getFirstName());
        _contact.setLastName(contact.getLastName());
        _contact.setEmail(contact.getEmail());
        _contact.setPhone(contact.getPhone());
        _contact.setPostalAddress(contact.getPostalAddress());
        return new ResponseEntity<>(contactRepository.save(_contact), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteContact(long id) {
        contactRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAllContacts() {
        contactRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
