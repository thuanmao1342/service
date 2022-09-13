package com.nvt.service.controllers;

import javax.naming.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.service.models.AuthRequest;
import com.nvt.service.models.AuthResponse;
import com.nvt.service.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws AuthenticationException {
        log.info("debug login");
        AuthResponse authResponse = authService.checkLogin(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
    
}
