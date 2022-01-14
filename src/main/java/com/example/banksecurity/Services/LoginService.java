package com.example.banksecurity.Services;

import com.example.banksecurity.DTOs.Request.LoginDTO;
import com.example.banksecurity.Security.JWT.JwtUtils;
import com.example.banksecurity.Security.UserDetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> authenticateUser(LoginDTO loginDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        System.out.println(roles);
        setCookies(response, jwt);
        return ResponseEntity.ok(jwt);
    }


    public void setCookies(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("token", jwt);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        //Makes the cookie only work on secure websites using https
        cookie.setSecure(false); //true on proper projects
        response.addCookie(cookie);
    }
}
