package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public void addStudent(@RequestBody @Validated StudentDTO studentDTO){
        studentService.addStudent(studentDTO);
    }

}
