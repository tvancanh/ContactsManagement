/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.safetrust.contacts.management.model.Contact;

/**
 * repository for App
 * @author tvancanh
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {

  Page<Contact> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String firstName, String lastName, Pageable pageable);

}
