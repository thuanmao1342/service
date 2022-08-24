package com.nvt.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tokens {
    
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
}
