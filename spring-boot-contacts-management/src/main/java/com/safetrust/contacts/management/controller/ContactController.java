/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.controller;

import java.util.Map;

import com.safetrust.contacts.management.ExceptionHandler.ResourceNotFoundException;
import com.safetrust.contacts.management.model.Contact;
import com.safetrust.contacts.management.repository.ContactRepository;
import com.safetrust.contacts.management.service.ContactService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    ContactService contactService;

    @ApiOperation(value = "Get all contacts")
    @GetMapping("/contact")
    public ResponseEntity<Map<String, Object>> getAllContactsPage(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        return contactService.getAllContacts(firstName, lastName, page, size);
    }

    @ApiOperation(value = "Get contact by id")
    @GetMapping("/contact/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") long id) {
        return contactService.getContactById(id);
    }

    @ApiOperation(value = "Create new contact")
    @PostMapping("/contact")
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) {
        return contactService.createContact(contact);
    }

    @ApiOperation(value = "Update contact by id")
    @PutMapping("/contact/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable("id") long id, @RequestBody Contact contact) {
        return contactService.updateContact(id, contact);
    }

    @ApiOperation(value = "Delete a contact")
    @DeleteMapping("/contact/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable("id") long id) {
        return contactService.deleteContact(id);
    }

    @ApiOperation(value = "Delete all contact")
    @DeleteMapping("/contacts")
    public ResponseEntity<HttpStatus> deleteAllContacts() {
        return contactService.deleteAllContacts();
    }

}
