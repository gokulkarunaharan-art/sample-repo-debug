package com.gokul.librarymanagement.service;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.mapper.StudentMapper;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import com.gokul.librarymanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;

    public List<StudentDTO> getAllStudents(){
        return studentRepository.findAll().stream().map(
                studentMapper::studentToStudentDTO
        ).toList();
    }

    public void addStudent(StudentDTO studentDTO) {
        studentRepository.save(studentMapper.studentDTOToStudent(studentDTO));
    }

    public void deleteStudent(UUID studentId){
        boolean hasActiceEntry = studentBookEntryRepository.findAllByStudent_Id(studentId)
                .stream().anyMatch(studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE);
        if(hasActiceEntry){

        }
    }
}
