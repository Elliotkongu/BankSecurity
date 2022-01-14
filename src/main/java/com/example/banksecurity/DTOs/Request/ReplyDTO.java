package com.example.banksecurity.DTOs.Request;

import lombok.Value;

@Value
public class ReplyDTO {
    String title;
    String text;
    Long senderId;
    Long receiverId;
}
