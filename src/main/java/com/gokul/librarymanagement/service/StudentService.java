package com.gokul.librarymanagement.service;


import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.DTO.StudentDTO;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;

    public Page<StudentDTO> getAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable).map(
                studentMapper::studentToStudentDTO
        );
    }

    public void addStudent(StudentDTO studentDTO) {
        if (studentRepository.countStudentByEmail(studentDTO.getEmail().toLowerCase()) > 0){
            throw new OperationNotAllowedException("student with mail already found!");
        }
        studentDTO.setEmail(studentDTO.getEmail().toLowerCase());
        studentRepository.save(studentMapper.studentDTOToStudent(studentDTO));
    }

    public void deleteStudent(UUID studentId){
        List<StudentBookEntry>  entries = getAllEntriesByStudent(studentId);
        boolean hasActiceEntry = entries
                .stream().anyMatch(studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE);
        if(hasActiceEntry){
            throw new OperationNotAllowedException("Student has active entries, cannot be deleted");
        }
        else{
            entries.forEach(studentBookEntry -> {studentBookEntry.setStudent(null);});
            studentBookEntryRepository.saveAll(entries);
            studentRepository.deleteById(studentId);
        }
    }
    public List<StudentBookEntry> getAllEntriesByStudent(UUID studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(()->new ResourceNotFoundException("Student with given id is not available"));
        return student.getStudentBookEntries().stream().toList();
    }
    public void uploadStudentCSV(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            CsvToBean<StudentCSVRepresentation> csvToBean = new CsvToBeanBuilder<StudentCSVRepresentation>(reader)
                    .withType(StudentCSVRepresentation.class)
                    .withVerifier(new StudentBeanVerifier())
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .build();

            List<StudentDTO> bookDTOS = csvToBean.parse().stream().map(line->{
                return StudentDTO.builder()
                        .email(line.getEmail())
                        .name(line.getName())
                        .phoneNumber(line.getPhoneNumber())
                        .build();
            }).toList();
            bookDTOS.forEach(this::addStudent);
        }}
}
