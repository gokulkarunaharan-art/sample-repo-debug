package com.gokul.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentBookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;

    private String bookTitle;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;

    private String studentName;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnedAt;

    private BorrowStatus status;
}
