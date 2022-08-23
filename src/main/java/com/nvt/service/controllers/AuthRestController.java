package com.nvt.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.service.models.AuthResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        // check login trong nay
        return ResponseEntity.ok(new AuthResponse());
    }
    
}
