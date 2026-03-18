package com.gokul.librarymanagement.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
