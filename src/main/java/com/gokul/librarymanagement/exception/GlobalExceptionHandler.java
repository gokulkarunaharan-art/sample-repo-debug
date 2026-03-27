package com.gokul.librarymanagement.exception;

import com.opencsv.exceptions.CsvConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex){
        Map<String, String> map = new HashMap<>();
        map.put("message: ",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Map<String, String>> handleOperationNotAllowedException(OperationNotAllowedException ex){
        Map<String, String> map = new HashMap<>();
        map.put("message: ",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler(CsvConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleCsvConstraintViolationException(CsvConstraintViolationException ex){
        Map<String, String> map = new HashMap<>();
        map.put("message: ",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
    @ExceptionHandler(CSVValidationException.class)
    public ResponseEntity<Map<String,String>> handleCSVValidationException(CSVValidationException ex){
        Map<String,String> response = new HashMap<>();
        ex.getExceptions().forEach(exception->{
            response.put("Row "+String.valueOf(exception.getLineNumber()),exception.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
