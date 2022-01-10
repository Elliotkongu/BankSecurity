package com.example.banksecurity.Controllers;

import com.example.banksecurity.Services.BankerService;
import com.example.banksecurity.Services.RegistrationService;
import com.example.banksecurity.Storage.Banker.Banker;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Banker>> getAllBankers() {
        return bankerService.getAllBankers();
    }

    @PutMapping("/opencustomeraccount/{regRequestId}")
    public ResponseEntity<String> openCustomerAccount(@PathVariable("regRequestId") Long registrationRequestId) {
        return registrationService.openCustomerAccount(registrationRequestId);
    }

    @GetMapping("/getregistrationrequests")
    public ResponseEntity<List<RegistrationRequest>> getAllRegistrationRequests() {
        return bankerService.getAllRegistrationRequests();
    }
}
