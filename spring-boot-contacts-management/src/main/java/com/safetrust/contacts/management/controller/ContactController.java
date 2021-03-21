/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.safetrust.contacts.management.model.Contact;
import com.safetrust.contacts.management.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
/**
 * Controller for App
 * @author tvancanh
 */
public class ContactController {

  @Autowired
  ContactRepository contactRepository;

  private boolean isEmpty(String str) {
    return str == null || "".equals(str);
  }

  @GetMapping("/contacts")
  public ResponseEntity<Map<String, Object>> getAllContactsPage(
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String lastName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size){

    try {
      List<Contact> contacts = new ArrayList<Contact>();
      Pageable pagingSort = PageRequest.of(page, size);

      Page<Contact> contactPage;
      if (isEmpty(firstName) && isEmpty(lastName)){
        contactPage = contactRepository.findAll(pagingSort);
      } else {
        contactPage = contactRepository
                .findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(firstName, lastName, pagingSort);
      }
      contacts = contactPage.getContent();
      Map<String, Object> response = new HashMap<>();
      response.put("name", contacts);
      response.put("currentPage", contactPage.getNumber());
      response.put("totalItems", contactPage.getTotalElements());
      response.put("totalPages", contactPage.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/contacts/{id}")
  public ResponseEntity<Contact> getContactById(@PathVariable("id") long id) {
    Optional<Contact> contactData = contactRepository.findById(id);

    if (contactData.isPresent()) {
      return new ResponseEntity<>(contactData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/contact")
  public ResponseEntity<Contact> createTutorial(@RequestBody Contact contact) {
    try {
      Contact _contact = contactRepository.save(new Contact(contact.getLastName(), contact.getFirstName(),
              contact.getEmail(), contact.getPhone(), contact.getPostalAddress()));
      return new ResponseEntity<>(_contact, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/contact/{id}")
  public ResponseEntity<Contact> updateTutorial(@PathVariable("id") long id, @RequestBody Contact contact) {
    Optional<Contact> contactData = contactRepository.findById(id);

    if (contactData.isPresent()) {
      Contact _contact = contactData.get();
      _contact.setFirstName(contact.getFirstName());
      _contact.setLastName(contact.getLastName());
      _contact.setEmail(contact.getEmail());
      _contact.setPhone(contact.getPhone());
      _contact.setPostalAddress(contact.getPostalAddress());
      return new ResponseEntity<>(contactRepository.save(_contact), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/contact/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    try {
      contactRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/contacts")
  public ResponseEntity<HttpStatus> deleteAllContacts() {
    try {
      contactRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
}
