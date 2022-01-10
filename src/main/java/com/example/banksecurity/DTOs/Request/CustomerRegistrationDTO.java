package com.example.banksecurity.DTOs.Request;

import lombok.Value;

@Value
public class CustomerRegistrationDTO {
    String firstName;
    String lastName;
    String username;
    String password;
}
