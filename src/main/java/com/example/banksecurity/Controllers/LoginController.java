package com.example.banksecurity.Controllers;

import com.example.banksecurity.DTOs.Request.LoginDTO;
import com.example.banksecurity.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/login")
public class LoginController {

    LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        return loginService.authenticateUser(loginDTO, response);
    }
}
