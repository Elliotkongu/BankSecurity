package com.example.banksecurity.Services;

import com.example.banksecurity.Storage.Banker.Banker;
import com.example.banksecurity.Storage.Banker.BankerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankerService {

    BankerRepository bankerRepository;

    @Autowired
    public BankerService (BankerRepository bankerRepository) {
        this.bankerRepository = bankerRepository;
    }

    public Banker addBanker(Long userId) {
        return bankerRepository.save(new Banker(userId));
    }

    public ResponseEntity<List<Banker>> getAllBankers() {
        return ResponseEntity.ok().body(bankerRepository.findAll());
    }
}
