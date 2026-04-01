package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.AuthRequestDTO;
import com.gokul.librarymanagement.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil = new JWTUtil();
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<Map<String,String>> generateToken(@RequestBody AuthRequestDTO authRequest){
        String userName = authRequest.getUsername();
        String password = authRequest.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtUtil.generateJWT(userName);
        Map<String,String> tokenResponse = new HashMap<>();
        tokenResponse.put("Token: ",token);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }
}
