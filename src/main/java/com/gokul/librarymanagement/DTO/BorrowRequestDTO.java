package com.gokul.librarymanagement.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class BorrowRequestDTO {
    @NotNull(message = "book_id should not be null")
    private UUID bookId;
    @NotNull(message = "student_id should not be null")
    private UUID studentID;
}
