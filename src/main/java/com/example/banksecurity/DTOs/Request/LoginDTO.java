package com.example.banksecurity.DTOs.Request;

import lombok.Value;

@Value
public class LoginDTO {
    String username;
    String password;
}
