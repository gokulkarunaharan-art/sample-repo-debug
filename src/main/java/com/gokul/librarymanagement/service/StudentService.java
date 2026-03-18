package com.gokul.librarymanagement.service;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.mapper.StudentMapper;
import com.gokul.librarymanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDTO> getAllStudents(){
        return studentRepository.findAll().stream().map(
                student -> {
                    return studentMapper.studentToStudentDTO(student);
                }
        ).toList();
    }
}
