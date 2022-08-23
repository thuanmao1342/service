package com.nvt.service.models;

import lombok.Data;

@Data
public class AuthResponse {
    
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String status;
    private String message;

}
