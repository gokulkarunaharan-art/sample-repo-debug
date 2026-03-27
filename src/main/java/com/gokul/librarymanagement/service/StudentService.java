package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.DTO.UploadSummaryDTO;
import com.gokul.librarymanagement.CSV.validation.StudentBeanVerifier;
import com.gokul.librarymanagement.CSV.StudentCSVRepresentation;
import com.gokul.librarymanagement.exception.CSVValidationException;
import com.gokul.librarymanagement.exception.OperationNotAllowedException;
import com.gokul.librarymanagement.exception.ResourceNotFoundException;
import com.gokul.librarymanagement.mapper.StudentMapper;
import com.gokul.librarymanagement.model.*;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import com.gokul.librarymanagement.repository.StudentRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;

    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(
                studentMapper::studentToStudentDTO
        );
    }

    public void addStudent(StudentDTO studentDTO) {
        if (studentRepository.countStudentByEmail(studentDTO.getEmail().toLowerCase()) > 0) {
            throw new OperationNotAllowedException("student with mail already found!");
        }
        studentDTO.setEmail(studentDTO.getEmail().toLowerCase());
        studentRepository.save(studentMapper.studentDTOToStudent(studentDTO));
    }

    public void deleteStudent(UUID studentId) {
        List<StudentBookEntry> entries = getAllEntriesByStudent(studentId);
        boolean hasActiceEntry = entries
                .stream().anyMatch(studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE);
        if (hasActiceEntry) {
            throw new OperationNotAllowedException("Student has active entries, cannot be deleted");
        } else {
            entries.forEach(studentBookEntry -> {
                studentBookEntry.setStudent(null);
            });
            studentBookEntryRepository.saveAll(entries);
            studentRepository.deleteById(studentId);
        }
    }

    public List<StudentBookEntry> getAllEntriesByStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with given id is not available"));
        return student.getStudentBookEntries().stream().toList();
    }

    public UploadSummaryDTO uploadStudentCSV(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("CSV file must not be empty");

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv"))
            throw new IllegalArgumentException("Only .csv files are accepted");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<StudentCSVRepresentation> csvToBean = new CsvToBeanBuilder<StudentCSVRepresentation>(reader)
                    .withType(StudentCSVRepresentation.class)
                    .withVerifier(new StudentBeanVerifier())
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withThrowExceptions(false)
                    .build();

            List<StudentCSVRepresentation> parsed = csvToBean.parse();

            //gathering all collected exceptions
            if (!csvToBean.getCapturedExceptions().isEmpty()) {
                throw new CSVValidationException(csvToBean.getCapturedExceptions());
            }

            int totalRows = parsed.size();

            // Step A — extract emails from CSV
            List<String> emails = parsed.stream()
                    .map(line -> line.getEmail().toLowerCase())
                    .toList();

            // Step B — one DB hit, scoped to only CSV candidates
            Set<String> existingEmails = studentRepository.findExistingEmails(emails);

            // Step C — filter and map in memory
            List<Student> students = parsed.stream()
                    .filter(line -> !existingEmails.contains(line.getEmail().toLowerCase()))
                    .map(line -> Student.builder()
                            .name(line.getName())
                            .email(line.getEmail().toLowerCase())
                            .phoneNumber(line.getPhoneNumber())
                            .build())
                    .toList();

            // Step D — batch insert
            studentRepository.saveAll(students);

            return new UploadSummaryDTO(totalRows, students.size(), totalRows - students.size());
        }
    }
}
