package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public void deleteBook(@PathVariable UUID bookId){
        bookService.deleteBook(bookId);
    }

    @PatchMapping("/{bookId}/decrement")
    public void decrementBook(@PathVariable("bookId") UUID bookId){
        bookService.decrementBook(bookId);
    }

    @PatchMapping("/{bookId}/increment")
    public void incrementBook(@PathVariable("bookId") UUID bookId){
        bookService.incrementBook(bookId);
    }
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public void uploadBookCSV(@RequestPart MultipartFile file) throws IOException {
        bookService.uploadBookCSV(file);
    }
}
