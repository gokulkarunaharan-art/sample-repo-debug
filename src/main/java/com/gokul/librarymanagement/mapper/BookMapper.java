package com.gokul.librarymanagement.mapper;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.model.Book;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO bookToBookDTO(Book book);
    Book bookDTOToBook(BookDTO bookDTO);
}
