package com.gokul.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<String> handleBookNotAvailableException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Book Not Available");
    }

    @ExceptionHandler(BorrowLimitExceededException.class)
    public ResponseEntity<String> handleBorrowLimitExceededException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot take two copies of the same book");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
    }
}
