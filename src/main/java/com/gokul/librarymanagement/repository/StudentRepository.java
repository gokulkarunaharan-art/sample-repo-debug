package com.gokul.librarymanagement.repository;

import com.gokul.librarymanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    int countStudentByEmail(String email);
    void deleteById(UUID id);
}
