package com.example.banksecurity.Controllers;

import com.example.banksecurity.Services.BankerService;
import com.example.banksecurity.Storage.Banker.Banker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/banker")
public class BankerController {
    BankerService bankerService;

    @Autowired
    public BankerController (BankerService bankerService) {
        this.bankerService = bankerService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Banker>> getAllBankers() {
        return bankerService.getAllBankers();
    }
}
