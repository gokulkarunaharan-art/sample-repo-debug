package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDTO> getAllStudents(){
        return studentService.getAllStudents();
    }

}
