package com.example.banksecurity.Controllers;

import com.example.banksecurity.DTOs.Request.CustomerRegistrationDTO;
import com.example.banksecurity.DTOs.Request.StartTransactionDTO;
import com.example.banksecurity.Services.CustomerService;
import com.example.banksecurity.Services.RegistrationService;
import com.example.banksecurity.Storage.Customer.Customer;
import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccount;
import com.example.banksecurity.Storage.Transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/customer")
public class CustomerController {

    CustomerService customerService;
    RegistrationService registrationService;

    @Autowired
    public CustomerController(CustomerService customerService, RegistrationService registrationService) {
        this.customerService = customerService;
        this.registrationService = registrationService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/{id}/addtomainaccount")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> addToMainAccount(@PathVariable("id") Long id, @RequestParam Double amount) {
        return customerService.addToMainAccount(id, amount);
    }

    @PutMapping("/{id}/subtractfrommainaccount")
    public ResponseEntity<String> subtractFromMainAccount(@PathVariable("id") Long id, @RequestParam Double amount) {
        return customerService.subtractFromMainAccount(id, amount);
    }

    @PutMapping("/{id}/addsavingsaccount")
    public ResponseEntity<String> addSavingsAccount(@PathVariable("id") Long id) {
        return customerService.addNewSavingsAccount(id);
    }

    @GetMapping("/{id}/getsavingsaccounts")
    public ResponseEntity<?> getAllSavingsAccounts(@PathVariable("id") Long id) {
        return customerService.getAllSavingsAccounts(id);
    }

    @PutMapping("/{id}/transfertosavings/{savingsIndex}")
    public ResponseEntity<String> transferToSavings(@PathVariable("id") Long id, @PathVariable("savingsIndex") Integer savingsIndex, @RequestParam String amount) {
        return customerService.transferToSavings(id, savingsIndex, new BigDecimal(amount));
    }

    @PutMapping("/{id}/transferfromsavings/{savingsIndex}")
    public ResponseEntity<String> transferFromSavings(@PathVariable("id") Long id, @PathVariable("savingsIndex") Integer savingsIndex, @RequestParam String amount) {
        return customerService.transferFromSavings(id, savingsIndex, new BigDecimal(amount));
    }

    @PostMapping("/register")
    public ResponseEntity<String> sendRegisterRequest(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        return registrationService.sendRegistrationRequest(customerRegistrationDTO);
    }

    @PostMapping("/starttransaction")
    public ResponseEntity<String> startTransaction(@RequestBody StartTransactionDTO startTransactionDTO) {
        return customerService.startTransaction(startTransactionDTO);
    }

    @GetMapping("/{id}/viewtransactions")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCustomerId(@PathVariable("id") Long id) {
        return customerService.getAllTransactionsByCustomerId(id);
    }
}
