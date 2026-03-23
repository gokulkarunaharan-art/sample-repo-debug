package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.StudentBookEntryDTO;
import com.gokul.librarymanagement.DTO.BorrowRequestDTO;
import com.gokul.librarymanagement.exception.BorrowLimitExceededException;
import com.gokul.librarymanagement.exception.ResourceNotFoundException;
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
                studentBookEntryMapper::toDTO
        ).toList();
    }

    public void studentBookEntryRequest(BorrowRequestDTO borrowRequestDTO){
        Student student = studentRepository.findById(borrowRequestDTO.getStudentID()).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with ID "+borrowRequestDTO.getStudentID()));
        Book book = bookRepository.findById(borrowRequestDTO.getBookId()).orElseThrow(
                () -> new ResourceNotFoundException("Book not found with ID "+borrowRequestDTO.getBookId()));

        //checking if the book is available
        if(book.getAvailableCopies() <= 0){
            throw new ResourceNotFoundException("No Available Copies of the requested book");
        }
        //checking if the user already taken the book
        if(studentBookEntryRepository.findByBookAndStudentAndStatus(book,student,BorrowStatus.ACTIVE).isPresent()){
            throw new BorrowLimitExceededException();
        }

        StudentBookEntry entry = StudentBookEntry.builder()
                .book(book)
                .bookTitle(book.getTitle())
                .student(student)
                .studentName(student.getName())
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
        else{
            throw new ResourceNotFoundException("Entry Not Found With ID "+entryID);
        }
    }

    public List<StudentBookEntryDTO> getAllActiveEntries() {
        return studentBookEntryRepository.findAllByStatus(BorrowStatus.ACTIVE).stream().map(
                studentBookEntryMapper::toDTO
        ).toList();
    }

    public List<StudentBookEntryDTO> getAllEntriesByBook(UUID bookID) {
       return studentBookEntryRepository.findAllByBook_Id(bookID).stream().map(
               studentBookEntryMapper::toDTO
       ).toList();
    }

    public List<StudentBookEntryDTO> getAllEntriesByStudent(UUID studentID) {
        return studentBookEntryRepository.findAllByStudent_Id(studentID).stream().map(
                studentBookEntryMapper::toDTO
        ).toList();
    }
}
//on branch csv sample