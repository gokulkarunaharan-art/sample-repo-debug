package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.service.BookService;
import com.gokul.librarymanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final BookService bookService;

    @GetMapping
    public Page<StudentDTO> getAllStudents(@PageableDefault(size = 25, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return studentService.getAllStudents(pageable);
    }

    @PostMapping
    public void addStudent(@RequestBody @Validated StudentDTO studentDTO){
        studentService.addStudent(studentDTO);
    }

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable UUID studentId){
        studentService.deleteStudent(studentId);
    }

    @PostMapping("/upload")
    public void uploadStudentCSV(@RequestPart MultipartFile file) throws IOException {
        studentService.uploadStudentCSV(file);
    }
}
