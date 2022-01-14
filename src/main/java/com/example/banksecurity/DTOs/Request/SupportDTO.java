package com.example.banksecurity.DTOs.Request;

import lombok.Value;

@Value
public class SupportDTO {
    String title;
    String text;
    Long senderId;
}
