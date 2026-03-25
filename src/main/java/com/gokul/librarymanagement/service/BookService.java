package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.exception.OperationNotAllowedException;
import com.gokul.librarymanagement.exception.ResourceNotFoundException;
import com.gokul.librarymanagement.mapper.BookMapper;
import com.gokul.librarymanagement.model.*;
import com.gokul.librarymanagement.repository.BookRepository;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;


    public Page<BookDTO> getAllBooks(Pageable pageable) {
        Page<Book> books =  bookRepository.findAll(pageable);
        return books.map(bookMapper::bookToBookDTO);
    }

    public List<BookDTO> getAllUnborrowedBooks() {
        return bookRepository.findBooksByAvailableCopiesGreaterThan(0)
                .stream().map(bookMapper::bookToBookDTO).toList();
    }

    public void addBook(BookDTO bookDTO) {
        Book book  = bookMapper.bookDTOToBook(bookDTO);
        book.setAvailableCopies(book.getTotalCopies());
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID bookId) {
        List<StudentBookEntry> entries = getAllEntriesByBook(bookId);

        boolean hasActiveBorrows = entries.stream().anyMatch(studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE);
        if (hasActiveBorrows) {
            throw new OperationNotAllowedException("Book has active borrows, cannot delete");
        }
        entries.forEach(e -> e.setBook(null));
        studentBookEntryRepository.saveAll(entries);
        bookRepository.deleteById(bookId);
    }

    public void decrementBook(UUID bookId) {
        //decrementing available copies
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new ResourceNotFoundException("Book Not Found"));
        if(book.getAvailableCopies() <= 0){
            throw new OperationNotAllowedException("Book Count is already 0, Cannot decrement");
        }
        book.setAvailableCopies(book.getAvailableCopies()-1);
        book.setTotalCopies(book.getTotalCopies()-1);
        bookRepository.save(book);
    }

    public void incrementBook(UUID bookId) {
        //incrementing available copies
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new ResourceNotFoundException("Book Not Found"));
        book.setAvailableCopies(book.getAvailableCopies()+1);

        //incrementing total copies
        book.setTotalCopies(book.getTotalCopies()+1);
        bookRepository.save(book);
    }


    public void uploadBookCSV(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            CsvToBean<BookCSVRepresentation> csvToBean = new CsvToBeanBuilder<BookCSVRepresentation>(reader)
                    .withType(BookCSVRepresentation.class)
                    .withVerifier(new BookBeanVerifier())
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .build();

            List<BookDTO> bookDTOS = csvToBean.parse().stream().map(line->{
                boolean exists = bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCase(
                        line.getTitle(), line.getAuthor());
                return BookDTO.builder()
                        .title(line.getTitle())
                        .author(line.getAuthor())
                        .totalCopies(line.getTotalCopies())
                        .build();
            }).toList();
            bookDTOS.forEach(this::addBook);
        }}

    public List<StudentBookEntry> getAllEntriesByBook(UUID bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Student with given id is not available"));
        return book.getStudentBookEntries().stream().toList();
    }
    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        Integer queryPageNumber;
        Integer queryPageSize;
        if(pageNumber == null){
            queryPageNumber = 0; //default page number
        }
        else{
            queryPageNumber = pageNumber -1;
        }

        if(pageSize == null){
            queryPageSize = 25;
        }
        else{
            if(pageSize > 1000){
                queryPageSize = 1000;
            }
            else{
                queryPageSize = pageSize;
            }
        }
       return PageRequest.of(queryPageNumber,queryPageSize);
    }
}
