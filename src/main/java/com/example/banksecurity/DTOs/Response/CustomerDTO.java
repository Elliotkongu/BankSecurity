package com.example.banksecurity.DTOs.Response;

import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccount;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class CustomerDTO {
    Long id;
    String firstName;
    String lastName;
    Long userId;
    String username;
    BigDecimal mainAccount;
    List<SavingsAccount> savingsAccountList;
}
