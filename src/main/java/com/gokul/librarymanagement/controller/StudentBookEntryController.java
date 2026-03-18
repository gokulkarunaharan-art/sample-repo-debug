package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.DTO.borrowRequestDTO;
import com.gokul.librarymanagement.service.StudentBookEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
public class StudentBookEntryController {

    private final StudentBookEntryService studentBookEntryService;


    @GetMapping
    public List<StudentBookEntryDTO> getAllRecords(){
        return studentBookEntryService.getAllStudentBookEntry();
    }

    @PostMapping
    public void borrow(@RequestBody borrowRequestDTO borrowRequestDTO){
        studentBookEntryService.studentBookEntryRequest(borrowRequestDTO);
    }

    @PostMapping("/return/{entryID}")
    public void returnBook(@PathVariable("entryID") UUID entryID){
        studentBookEntryService.returnBook(entryID);
    }
}
