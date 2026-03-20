package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.mapper.BookMapper;
import com.gokul.librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll().stream().map(
                bookMapper::bookToBookDTO
        ).toList();
    }

    public List<BookDTO> getAllUnborrowedBooks(){
        return bookRepository.findBooksByAvailableCopiesGreaterThan(0).stream().map(
                book -> {return bookMapper.bookToBookDTO(book);}
        ).toList();
    }

    public void addBook(BookDTO bookDTO) {
        bookRepository.save(bookMapper.bookDTOToBook(bookDTO));
    }
}
