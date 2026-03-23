package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.exception.OperationNotAllowedException;
import com.gokul.librarymanagement.exception.ResourceNotFoundException;
import com.gokul.librarymanagement.mapper.BookMapper;
import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.model.StudentBookEntry;
import com.gokul.librarymanagement.repository.BookRepository;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;


    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC,"availableCopies")).stream().map(
                bookMapper::bookToBookDTO
        ).toList();
    }

    public List<BookDTO> getAllUnborrowedBooks(){
        return bookRepository.findBooksByAvailableCopiesGreaterThan(0).stream().map(
                book -> {return bookMapper.bookToBookDTO(book);}
        ).toList();
    }

    public void addBook(BookDTO bookDTO) {
        Book book  = bookMapper.bookDTOToBook(bookDTO);
        book.setAvailableCopies(book.getTotalCopies());
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID bookId) {
        List<StudentBookEntry> entries = studentBookEntryRepository.findAllByBook_Id(bookId);

        boolean hasActiveBorrows = entries
                .stream().anyMatch(
                        studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE
                                    );
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
}
