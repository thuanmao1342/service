package com.nvt.service.models;

import lombok.Data;

@Data
public class AuthRequest {
    
    private String username;
    private String password;
    private String captcha;
    private String captchaKey;

}
