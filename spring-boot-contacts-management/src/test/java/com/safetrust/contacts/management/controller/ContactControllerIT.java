/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.controller;

import com.safetrust.contacts.management.ContactManagementApplication;
import com.safetrust.contacts.management.model.Contact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tvancanh
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContactManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerIT {
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;
    HttpEntity<String> entity;

    @Before
    public void setUp(){
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        entity = new HttpEntity<String>(null, headers);
    }



    private String createBaseUrl(String uri) {
        return "http://localhost:8181/api" + uri;
    }

    @Test
    public void getAllContact_statusCode200_responseNotNull() {
        //Act
        ResponseEntity<String> response = restTemplate.exchange(
                createBaseUrl("/contact"),
                HttpMethod.GET, entity, String.class);
        //Assert
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getContactById_statusCode200_responseNotNull() {
        //Act
        ResponseEntity<String> response = restTemplate.exchange(
                createBaseUrl("/contact/1"),
                HttpMethod.GET, entity, String.class);
        //Assert
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void addContact_statusCode200_responseNotNull() {
        //Arrange
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        HttpEntity<Contact> entity = new HttpEntity<Contact>(contact, headers);
        //Act
        ResponseEntity<String> response = restTemplate.exchange(
                createBaseUrl("/contact"),
                HttpMethod.POST, entity, String.class);
        //Assert
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void updateContact_statusCode200_responseNotNull(){
        //Arrange
        Contact contact = new Contact("tran", "canh", "canhtranhg96@gmail.com", "0123456789", "HCM");
        HttpEntity<Contact> entity = new HttpEntity<Contact>(contact, headers);
        //Act
        ResponseEntity<String> response = restTemplate.exchange(
                createBaseUrl("/contact/1"),
                HttpMethod.PUT, entity, String.class);
        //Assert
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteContact_responseNotNull() {
        //Act
        ResponseEntity<String> response = restTemplate.exchange(
                createBaseUrl("/contact/12"),
                HttpMethod.DELETE, entity, String.class);
        //Assert
        ResponseEntity<String> resp = restTemplate.exchange(
                createBaseUrl("/contact/12"),
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    public void deleteAllContacts(){
        //Act
       restTemplate.exchange(
                createBaseUrl("/contacts/"),
                HttpMethod.DELETE, entity, String.class);
        //Assert
        ResponseEntity<String> resp = restTemplate.exchange(
                createBaseUrl("/contact/"),
                HttpMethod.GET, entity, String.class);
        Assert.assertNull(resp.getBody());
    }

}