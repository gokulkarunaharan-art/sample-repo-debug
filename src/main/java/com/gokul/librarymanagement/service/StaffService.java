package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.exception.OperationNotAllowedException;
import com.gokul.librarymanagement.model.Role;
import com.gokul.librarymanagement.model.Staff;
import com.gokul.librarymanagement.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public void createStaff(String username, String password) {
        if (staffRepository.existsByUserName(username)) {
            throw new OperationNotAllowedException("staff with provided username already exists");
        }
        Staff newStaff = Staff.builder()
                .userName(username)
                .password(passwordEncoder.encode(password))
                .role(Role.STAFF).build();
        staffRepository.save(newStaff);
    }
}
