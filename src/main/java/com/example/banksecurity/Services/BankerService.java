package com.example.banksecurity.Services;

import com.example.banksecurity.Storage.Banker.Banker;
import com.example.banksecurity.Storage.Banker.BankerRepository;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequest;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankerService {

    BankerRepository bankerRepository;
    RegistrationRequestRepository registrationRequestRepository;
    @Autowired
    public BankerService (BankerRepository bankerRepository, RegistrationRequestRepository registrationRequestRepository) {
        this.bankerRepository = bankerRepository;
        this.registrationRequestRepository = registrationRequestRepository;
    }

    public Banker addBanker(Long userId) {
        return bankerRepository.save(new Banker(userId));
    }

    public ResponseEntity<List<Banker>> getAllBankers() {
        return ResponseEntity.ok().body(bankerRepository.findAll());
    }

    public ResponseEntity<List<RegistrationRequest>> getAllRegistrationRequests() {
        return ResponseEntity.ok().body(registrationRequestRepository.findAll());
    }
}
