package com.gokul.librarymanagement.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BorrowLimitExceededException extends RuntimeException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
