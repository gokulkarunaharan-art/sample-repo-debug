package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.DTO.borrowRequestDTO;
import com.gokul.librarymanagement.exception.BookNotAvailableException;
import com.gokul.librarymanagement.exception.BorrowLimitExceededException;
import com.gokul.librarymanagement.mapper.StudentBookEntryMapper;
import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.model.Student;
import com.gokul.librarymanagement.model.StudentBookEntry;
import com.gokul.librarymanagement.repository.BookRepository;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import com.gokul.librarymanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentBookEntryService {

    private final StudentBookEntryRepository studentBookEntryRepository;
    private final StudentBookEntryMapper studentBookEntryMapper;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public List<StudentBookEntryDTO> getAllStudentBookEntry(){
        return studentBookEntryRepository.findAll().stream().map(
                studentBookEntry->{
                    return studentBookEntryMapper.toDTO(studentBookEntry);
                }
        ).toList();
    }

    public void studentBookEntryRequest(borrowRequestDTO borrowRequestDTO){
        Student student = studentRepository.findById(borrowRequestDTO.getStudentID()).orElse(null);
        //should handle the null student case
        Book book = bookRepository.findById(borrowRequestDTO.getBookId()).orElseThrow(()->new BookNotAvailableException());
        //should handle the null book case

        //checking if the user already taken the book
        if(studentBookEntryRepository.findByBookAndStudentAndStatus(book,student,BorrowStatus.ACTIVE).isPresent()){
            throw new BorrowLimitExceededException();
        }

        //checking if the book is available
        if(book.getAvailableCopies() <= 0){
            throw new BookNotAvailableException();
        }

        StudentBookEntry entry = StudentBookEntry.builder()
                .book(book)
                .student(student)
                .borrowedAt(LocalDateTime.now())
                .returnedAt(null)
                .status(BorrowStatus.ACTIVE)
                .build();

        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);
        studentBookEntryRepository.save(entry);
    }

    public void returnBook(UUID entryID){

        if(studentBookEntryRepository.findById(entryID).isPresent()){
            StudentBookEntry entry = studentBookEntryRepository.findById(entryID).get();
            Book book =  entry.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            entry.setReturnedAt(LocalDateTime.now());
            entry.setStatus(BorrowStatus.RETURNED);
            studentBookEntryRepository.save(entry);
        }
        // handle or throw entry not found exception
    }
}
