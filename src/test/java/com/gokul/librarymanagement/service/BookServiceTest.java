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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Mock
    StudentBookEntryRepository studentBookEntryRepository;

    @InjectMocks
    BookService bookService;

    @Test
    public void getAllBooks_whenBookNameIsNull_ShouldCallFindAll(){
        //arrange
        Pageable pageable =  PageRequest.of(0,10);
        Page<Book> mockPage = Mockito.mock(Page.class);
        //act
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(mockPage);
        Mockito.when(mockPage.map(Mockito.any())).thenReturn(Page.empty());
        bookService.getAllBooks(pageable,null);

        //assert
        Mockito.verify(bookRepository,Mockito.times(1)).findAll(pageable);
        Mockito.verify(bookRepository,Mockito.never()).findBooksByTitleContainingIgnoreCase(Mockito.any(),Mockito.any());
    }

    @Test
    public void getAllBooks_whenBookNameIsNotNull_shouldCallFindBooksByTitleContainingIgnoreCase(){
        //arrange
        Pageable pageable =  PageRequest.of(0,10);
        Page<Book> mockPage = Mockito.mock(Page.class);

        //act
        Mockito.when(bookRepository.findBooksByTitleContainingIgnoreCase(Mockito.any(),Mockito.any())).thenReturn(mockPage);
        Mockito.when(mockPage.map(Mockito.any())).thenReturn(Page.empty());
        bookService.getAllBooks(pageable,"sampleName");

        //assert
        Mockito.verify(bookRepository,Mockito.never()).findAll(Mockito.any(Pageable.class));
        Mockito.verify(bookRepository,Mockito.times(1)).findBooksByTitleContainingIgnoreCase(Mockito.any(),Mockito.any());
    }

    @Test
    public void getAllUnborrowedBooks_shouldReturnMappedDTOs(){
        //arrange
        Book book = Book.builder()
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .availableCopies(5)
                .build();
        BookDTO bookDTO = BookDTO.builder()
                .title("testBook")
                .author("sampleAuthor")
                .availableCopies(5)
                .build();
        //act
        Mockito.when(bookRepository.findBooksByAvailableCopiesGreaterThan(0)).thenReturn(List.of(book));
        Mockito.when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);
        List<BookDTO> returnedBookDTO = bookService.getAllUnborrowedBooks();

        //assert
        Assertions.assertNotNull(returnedBookDTO);
        Assertions.assertEquals(returnedBookDTO,List.of(bookDTO));

    }

    @Test
    public void getAllUnborrowedBooks_WhenNoBooksAvailable_ShouldReturnEmptyList(){
        //arrange
        BookDTO bookDTO = BookDTO.builder()
                .title("testBook")
                .author("sampleAuthor")
                .availableCopies(5)
                .build();
        //act
        Mockito.when(bookRepository.findBooksByAvailableCopiesGreaterThan(0)).thenReturn(Collections.emptyList());
        List<BookDTO> returnedBookDTO = bookService.getAllUnborrowedBooks();

        //assert
        assertThat(returnedBookDTO).isNotNull();

    }

    @Test
    public void addBook_AvailableCopies_SetToTotalCopies(){
        //arrange
        Book book = Book.builder()
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .build();
        BookDTO bookDTO = BookDTO.builder()
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .build();
        //act
        Mockito.when(bookMapper.bookDTOToBook(bookDTO)).thenReturn(book);
        Mockito.when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);
        assertThat(book.getAvailableCopies()).isNull();
        bookService.addBook(bookDTO);

        assertThat(book.getAvailableCopies()).isNotNull();
        assertThat(book.getAvailableCopies()).isEqualTo(book.getTotalCopies());
        Mockito.verify(bookRepository,Mockito.times(1)).save(book);
    }

    @Test
    public void getBookById_whenBookPresent_returnsBookDTO(){
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .availableCopies(5)
                .build();
        BookDTO responseBookDTO = BookDTO.builder()
                .title("testBook")
                .author("sampleAuthor")
                .build();
        Mockito.when(bookRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.bookToBookDTO(book)).thenReturn(responseBookDTO);
        BookDTO returnedBookDTO = bookService.getBookById(book.getId());
        assertThat(returnedBookDTO).isNotNull();
    }

    @Test
    public void getBookById_whenBookNotPresent_throwsException(){
        UUID randomUUID = UUID.randomUUID();

        Mockito.when(bookRepository.findById(randomUUID)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->bookService.getBookById(randomUUID));
    }

    @Test
    public void deleteBook_whenNoActiveEntries_setEntryBooksToNull() {
        //arrange

        StudentBookEntry studentBookEntry = StudentBookEntry.builder()
                .bookTitle("sampleBookTitle")
                .studentName("sampleStudentName")
                .borrowedAt(LocalDateTime.now())
                .returnedAt(LocalDateTime.now())
                .status(BorrowStatus.RETURNED)
                .build();

        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .studentBookEntries(Set.of(studentBookEntry))
                .totalCopies(5)
                .availableCopies(5)
                .build();
        studentBookEntry.setBook(book);

        //act
        Mockito.when(bookRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(book));
        bookService.deleteBook(book.getId());
        assertThat(studentBookEntry.getBook()).isNull();
        Mockito.verify(studentBookEntryRepository, Mockito.times(1)).saveAll(Mockito.any());
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void deleteBook_ActiveEntries_throwsException() {

        StudentBookEntry studentBookEntry = StudentBookEntry.builder()
                .bookTitle("sampleBookTitle")
                .studentName("sampleStudentName")
                .borrowedAt(LocalDateTime.now())
                .returnedAt(LocalDateTime.now())
                .status(BorrowStatus.ACTIVE)
                .build();

        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .studentBookEntries(Set.of(studentBookEntry))
                .totalCopies(5)
                .availableCopies(5)
                .build();

        Mockito.when(bookRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(book));
        assertThrows(OperationNotAllowedException.class, () -> bookService.deleteBook(book.getId()));
    }

    @Test
    public void decrementBook_bookNotFound_throwsException(){
        assertThrows(ResourceNotFoundException.class,()->bookService.decrementBook(UUID.randomUUID()));
    }


    @Test
    public void decrementBook_AvailableCopiesGt1_decrementAvailableandTotalCopies(){
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .availableCopies(5)
                .build();
        Integer oldAvailableCopies = book.getAvailableCopies();
        Integer oldTotalCopies = book.getTotalCopies();
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.decrementBook(book.getId());
        assertThat(book.getAvailableCopies()).isEqualTo(oldAvailableCopies-1);
        assertThat(book.getTotalCopies()).isEqualTo(oldTotalCopies-1);
        Mockito.verify(bookRepository,Mockito.times(1)).save(book);
    }

    @Test
    public void decrementBook_availableCopiesEq0_throwsException(){
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .availableCopies(0)
                .build();
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertThrows(OperationNotAllowedException.class,()->bookService.decrementBook(book.getId()));
    }

    @Test
    public void incrementBook_bookNotFound_throwsException(){
        assertThrows(ResourceNotFoundException.class,()->bookService.incrementBook(UUID.randomUUID()));
    }

    @Test
    public void incrementBook_bookFound_incrementAvailableAndTotalCopies(){
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .totalCopies(5)
                .availableCopies(5)
                .build();
        Integer oldAvailableCopies = book.getAvailableCopies();
        Integer oldTotalCopies = book.getTotalCopies();
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.incrementBook(book.getId());
        assertThat(book.getAvailableCopies()).isEqualTo(oldAvailableCopies+1);
        assertThat(book.getTotalCopies()).isEqualTo(oldTotalCopies+1);
        Mockito.verify(bookRepository,Mockito.times(1)).save(book);
    }

    @Test
    public void getAllEntriesByBook_bookNotFound_throwsException(){
        Mockito.when(bookRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->bookService.getAllEntriesByBook(UUID.randomUUID()));
    }

    @Test
    public void getAllEntriesByBook_bookFound_returnsStudentBookEntryList(){
        StudentBookEntry studentBookEntry = StudentBookEntry.builder()
                .bookTitle("sampleBookTitle")
                .studentName("sampleStudentName")
                .borrowedAt(LocalDateTime.now())
                .returnedAt(LocalDateTime.now())
                .status(BorrowStatus.ACTIVE)
                .build();

        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title("testBook")
                .author("sampleAuthor")
                .studentBookEntries(Set.of(studentBookEntry))
                .totalCopies(5)
                .availableCopies(5)
                .build();

        Mockito.when(bookRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(book));
        List<StudentBookEntry> returnedList = bookService.getAllEntriesByBook(UUID.randomUUID());
        assertThat(returnedList).isEqualTo(List.of(studentBookEntry));
    }



}
