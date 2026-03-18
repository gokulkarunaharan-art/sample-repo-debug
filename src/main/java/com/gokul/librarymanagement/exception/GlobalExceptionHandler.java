package com.gokul.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<String> handleBookNotAvailableException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Book Not Available");
    }

    @ExceptionHandler(BorrowLimitExceededException.class)
    public ResponseEntity<String> handleBorrowLimitExceededException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot take two copies of the same book");
    }
}
