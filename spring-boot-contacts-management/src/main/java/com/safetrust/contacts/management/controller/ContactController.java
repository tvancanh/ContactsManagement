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

import com.safetrust.contacts.management.ExceptionHandler.ResourceNotFoundException;
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

import javax.validation.Valid;

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

      if(contacts.isEmpty()){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      Map<String, Object> response = new HashMap<>();
      response.put("name", contacts);
      response.put("currentPage", contactPage.getNumber());
      response.put("totalItems", contactPage.getTotalElements());
      response.put("totalPages", contactPage.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/contacts/{id}")
  public ResponseEntity<Contact> getContactById(@PathVariable("id") long id) {
    Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("id contact not found: " +id));

      return new ResponseEntity<>(contact, HttpStatus.OK);

  }

  @PostMapping("/contact")
  public ResponseEntity createTutorial(@Valid @RequestBody Contact contact) {
      Contact _contact = contactRepository.save(new Contact(contact.getLastName(), contact.getFirstName(),
              contact.getEmail(), contact.getPhone(), contact.getPostalAddress()));
      return new ResponseEntity<>(_contact, HttpStatus.OK);
  }

  @PutMapping("/contact/{id}")
  public ResponseEntity<Contact> updateTutorial(@PathVariable("id") long id, @RequestBody Contact contact) {
    Contact _contact = contactRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("id contact not found: " + id));
      _contact.setFirstName(contact.getFirstName());
      _contact.setLastName(contact.getLastName());
      _contact.setEmail(contact.getEmail());
      _contact.setPhone(contact.getPhone());
      _contact.setPostalAddress(contact.getPostalAddress());
      return new ResponseEntity<>(contactRepository.save(_contact), HttpStatus.OK);
  }

  @DeleteMapping("/contact/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
      contactRepository.deleteById(id);

      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/contacts")
  public ResponseEntity<HttpStatus> deleteAllContacts() {
      contactRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
