package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/available")
    public List<BookDTO> getAllAvailableBooks(){
        return bookService.getAllUnborrowedBooks();
    }

    @PostMapping
    public void addBook(@RequestBody @Validated BookDTO bookDTO){
        bookService.addBook(bookDTO);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") UUID bookId){

    }
}
