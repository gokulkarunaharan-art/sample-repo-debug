package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.AuthRequestDTO;
import com.gokul.librarymanagement.model.Role;
import com.gokul.librarymanagement.model.Staff;
import com.gokul.librarymanagement.repository.StaffRepository;
import com.gokul.librarymanagement.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    private final PasswordEncoder encoder;

    private  JWTUtil jwtUtil = new JWTUtil();

    @PostMapping
    public ResponseEntity<Map<String,String>> generateToken(@RequestBody AuthRequestDTO authRequest){
        String userName = authRequest.getUsername();
        String password = authRequest.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,password);
        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtil.generateJWT(userName);
        Map<String,String> tokenResponse = new HashMap<>();
        tokenResponse.put("Token: ",token);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @PostMapping("/staff")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity addStaff(@RequestBody AuthRequestDTO authRequestDTO){
        Staff newStaff = Staff.builder()
                .userName(authRequestDTO.getUsername())
                .password(encoder.encode(authRequestDTO.getPassword()))
                .role(Role.STAFF)
                .build();
        Staff savedStaff = staffRepository.save(newStaff);
        return ResponseEntity.ok("Staff created");
    }
}
