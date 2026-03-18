package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.service.StudentBookEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class StudentBookEntryController {

    private final StudentBookEntryService studentBookEntryService;


    @GetMapping
    public List<StudentBookEntryDTO> getAllRecords(){
        return studentBookEntryService.getAllRecords();
    }
}
