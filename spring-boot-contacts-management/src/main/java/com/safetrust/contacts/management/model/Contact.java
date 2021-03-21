/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

   @Pattern(regexp="^(.+)@(.+)$",
          message="Invalid email address")
    @Column(name = "email_address")
    private String email;

    @Pattern(regexp="\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}",
          message="Must be formatted 1234567890 or 123-456-7890 or (123)456-7890 or (123)4567890")
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "postal_address")
    private String postalAddress;

  public Contact(String lastName, @NotNull String firstName, @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$",
          message = "Invalid email address") String email, @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}",
          message = "Must be formatted 1234567890 or 123-456-7890 or (123)456-7890 or (123)4567890") String phone, @NotNull String postalAddress) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.phone = phone;
    this.postalAddress = postalAddress;
  }

  public Contact(String firstName, String lastName, String email, String postalAddress) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.postalAddress = postalAddress;
  }

  @Override
    public String toString() {
        return "Contact [id=" + id + ", name=" + firstName + lastName + ", email=" + email + ", " +
                "phone=" + phone + ", postal address=" + postalAddress + "]";
    }

}
