package com.gokul.librarymanagement.mapper;


import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.model.StudentBookEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentBookEntryMapper {

    @Mapping(source = "book.title", target = "bookName")
    @Mapping(source = "student.name", target = "studentName")
    StudentBookEntryDTO toDTO(StudentBookEntry studentBookEntry);

}
