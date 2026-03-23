package com.gokul.librarymanagement.mapper;


import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.model.StudentBookEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentBookEntryMapper {

    @Mapping(source = "book.id", target = "bookID")
    StudentBookEntryDTO toDTO(StudentBookEntry studentBookEntry);

}
