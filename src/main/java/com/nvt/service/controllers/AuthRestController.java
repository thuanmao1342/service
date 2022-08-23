package com.nvt.service.controllers;

import javax.naming.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.service.models.AuthResponse;
import com.nvt.service.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam("username") String username,
            @RequestParam("password") String password) throws AuthenticationException {
        // check login trong nay
        log.info("username :{}", username);
        AuthResponse authResponse = authService.checkLogin(username, password);
        return ResponseEntity.ok(authResponse);
    }
    
}
