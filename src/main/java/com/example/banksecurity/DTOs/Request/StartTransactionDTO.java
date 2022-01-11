package com.example.banksecurity.DTOs.Request;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class StartTransactionDTO {
    Long customerFrom;
    Long customerTo;
    BigDecimal amount;
}
