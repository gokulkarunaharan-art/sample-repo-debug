package com.gokul.librarymanagement.mapper;


import com.gokul.librarymanagement.DTO.StudentDTO;
import com.gokul.librarymanagement.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO studentToStudentDTO(Student student);
    Student studentDTOToStudent(StudentDTO studentDTO);
}
