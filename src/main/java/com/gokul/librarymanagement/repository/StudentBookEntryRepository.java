package com.gokul.librarymanagement.repository;


import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.model.Student;
import com.gokul.librarymanagement.model.StudentBookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentBookEntryRepository extends JpaRepository<StudentBookEntry, UUID> {

    List<StudentBookEntry> findByStudentId(UUID studentId);

    Optional<StudentBookEntry> findByBookAndStudentAndStatus(
            Book book, Student student, BorrowStatus status
    );
}