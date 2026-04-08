package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.AuthRequestDTO;
import com.gokul.librarymanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Map<String,String>> generateToken(@RequestBody AuthRequestDTO authRequest){
        Map<String,String> tokenResponse = new HashMap<>();
        tokenResponse.put("token",authService.authenticateUser(authRequest.getUsername(),authRequest.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }
}
