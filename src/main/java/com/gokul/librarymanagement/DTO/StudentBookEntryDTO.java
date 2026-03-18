package com.gokul.librarymanagement.DTO;

import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.model.Student;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentBookEntryDTO {


    private Book bookTitle;

    private Student studentName;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private BorrowStatus status;
}
