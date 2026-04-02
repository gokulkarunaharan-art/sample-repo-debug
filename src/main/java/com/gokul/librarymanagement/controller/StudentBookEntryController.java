package com.gokul.librarymanagement.controller;


import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.DTO.BorrowRequestDTO;
import com.gokul.librarymanagement.service.StudentBookEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
    public void borrow(@RequestBody @Validated BorrowRequestDTO borrowRequestDTO){
        studentBookEntryService.studentBookEntryRequest(borrowRequestDTO);
    }

    @PostMapping("/return/{entryID}")
    public void returnBook(@PathVariable("entryID") UUID entryID){
        studentBookEntryService.returnBook(entryID);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<StudentBookEntryDTO> getAllActiveEntries(){
        return studentBookEntryService.getAllActiveEntries();
    }

    @GetMapping("/book/{book_id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<StudentBookEntryDTO> getAllEntriesForBook(@PathVariable("book_id") UUID bookID){
        return studentBookEntryService.getAllEntriesByBook(bookID);
    }

    @GetMapping("/student/{student_id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<StudentBookEntryDTO> getAllEntriesForStudent(@PathVariable("student_id") UUID studentID){
        return studentBookEntryService.getAllEntriesByStudent(studentID);
    }
}
