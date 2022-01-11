package com.example.banksecurity.DTOs.Response;

import lombok.Value;

@Value
public class RegistrationRequestDTO {
    Long id;
    String firstName;
    String lastName;
    Boolean resolved;
}
