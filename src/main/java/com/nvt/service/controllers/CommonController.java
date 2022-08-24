package com.nvt.service.controllers;

import com.amazonaws.HttpMethod;
import com.nvt.service.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cm")
@RequiredArgsConstructor
public class CommonController {

    private final S3Service s3Service;

    @GetMapping("/pre-signUrl")
    public String preSignUrlUpload(){
        String url = s3Service.generatePreSignedUrl("/upload/avatar","s3-nvt", HttpMethod.PUT );
        return url;
    }
}
