package com.gokul.librarymanagement.controller;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.DTO.UploadSummaryDTO;
import com.gokul.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Page<BookDTO> getAllBooks
            (@PageableDefault(size = 25, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
             @RequestParam(required = false, name = "bookName") String bookName
            ) {
        return bookService.getAllBooks(pageable,bookName);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("bookId") UUID bookId){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookById(bookId));
    }

    @GetMapping("/available")
    public List<BookDTO> getAllAvailableBooks() {
        return bookService.getAllUnborrowedBooks();
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody @Validated BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDTO));
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
    }

    @PatchMapping("/{bookId}/decrement")
    public void decrementBook(@PathVariable("bookId") UUID bookId) {
        bookService.decrementBook(bookId);
    }

    @PatchMapping("/{bookId}/increment")
    public void incrementBook(@PathVariable("bookId") UUID bookId) {
        bookService.incrementBook(bookId);
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadSummaryDTO> uploadBookCSV(@RequestPart MultipartFile file) throws IOException {
       return ResponseEntity.status(HttpStatus.OK).body(bookService.uploadBookCSV(file));
    }
}
