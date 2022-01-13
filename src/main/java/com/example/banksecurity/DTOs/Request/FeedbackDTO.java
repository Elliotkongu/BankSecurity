package com.example.banksecurity.DTOs.Request;

import lombok.Value;

@Value
public class FeedbackDTO {
    String title;
    String text;
    Long senderId;
}
