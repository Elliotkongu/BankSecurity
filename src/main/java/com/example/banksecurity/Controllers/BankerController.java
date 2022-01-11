package com.example.banksecurity.Controllers;

import com.example.banksecurity.DTOs.Response.RegistrationRequestDTO;
import com.example.banksecurity.Services.BankerService;
import com.example.banksecurity.Services.RegistrationService;
import com.example.banksecurity.Storage.Banker.Banker;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/banker")
public class BankerController {
    BankerService bankerService;
    RegistrationService registrationService;
    @Autowired
    public BankerController (BankerService bankerService, RegistrationService registrationService) {
        this.bankerService = bankerService;
        this.registrationService = registrationService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Banker>> getAllBankers() {
        return bankerService.getAllBankers();
    }

    @PutMapping("/opencustomeraccount/{regRequestId}")
    @PreAuthorize("hasRole('BANKER')")
    public ResponseEntity<String> openCustomerAccount(@PathVariable("regRequestId") Long registrationRequestId) {
        return registrationService.openCustomerAccount(registrationRequestId);
    }

    @GetMapping("/getregistrationrequests")
    @PreAuthorize("hasRole('BANKER') or hasRole('ADMIN')")
    public ResponseEntity<List<RegistrationRequestDTO>> getAllRegistrationRequests() {
        return bankerService.getAllRegistrationRequests();
    }

    @PutMapping("handletransaction/{id}")
    @PreAuthorize("hasRole('BANKER')")
    public ResponseEntity<String> handleTransaction(@PathVariable("id") Long transactionId) {
        return bankerService.handleTransaction(transactionId);
    }
}
