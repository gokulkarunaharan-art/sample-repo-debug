package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks(){
        return bookService.getAllBooks();
    }

}
