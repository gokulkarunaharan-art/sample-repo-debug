package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.AuthRequestDTO;
import com.gokul.librarymanagement.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity addStaff(@RequestBody AuthRequestDTO authRequestDTO){
        staffService.createStaff(authRequestDTO.getUsername(),authRequestDTO.getPassword());
        return ResponseEntity.ok("Staff created");
    }
}
