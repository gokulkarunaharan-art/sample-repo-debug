package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.AuthRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @PostMapping
    public ResponseEntity<Map<String,String>> generateToken(@RequestBody AuthRequestDTO authRequest){
        return null;
    }
}
