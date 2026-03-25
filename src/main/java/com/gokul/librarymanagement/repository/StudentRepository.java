package com.gokul.librarymanagement.repository;

import com.gokul.librarymanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    int countStudentByEmail(String email);
    @Query("SELECT LOWER(s.email) FROM Student s WHERE LOWER(s.email) IN :emails")
    Set<String> findExistingEmails(@Param("emails") List<String> emails);

}
