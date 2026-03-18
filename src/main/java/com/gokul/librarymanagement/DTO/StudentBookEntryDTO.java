package com.gokul.librarymanagement.DTO;

import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentBookEntryDTO {

    private UUID id;

    private UUID bookID;

    private String bookTitle;

    private String studentName;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private BorrowStatus status;
}
