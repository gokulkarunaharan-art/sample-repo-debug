package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.exception.OperationNotAllowedException;
import com.gokul.librarymanagement.mapper.BookMapper;
import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.model.BorrowStatus;
import com.gokul.librarymanagement.repository.BookRepository;
import com.gokul.librarymanagement.repository.StudentBookEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;


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
        Book book  = bookMapper.bookDTOToBook(bookDTO);
        book.setAvailableCopies(book.getTotalCopies());
        bookRepository.save(book);
    }

    public void deleteBook(UUID bookId) {
        boolean hasActiveBorrows = studentBookEntryRepository
                .findAllByBook_Id(bookId)
                .stream().anyMatch(
                        studentBookEntry -> studentBookEntry.getStatus() == BorrowStatus.ACTIVE
                                    );
        if (hasActiveBorrows) {
            throw new OperationNotAllowedException("Book has active borrows, cannot delete");
        }
        bookRepository.deleteById(bookId);
    }
}
