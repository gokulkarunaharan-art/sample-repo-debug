package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.CSV.BookCSVRepresentation;
import com.gokul.librarymanagement.DTO.BookDTO;
import com.gokul.librarymanagement.DTO.UploadSummaryDTO;
import com.gokul.librarymanagement.CSV.validation.BookBeanVerifier;
import com.gokul.librarymanagement.exception.CSVValidationException;
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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StudentBookEntryRepository studentBookEntryRepository;


    public Page<BookDTO> getAllBooks(Pageable pageable,String bookName) {

        Page<Book> books;
        if(bookName != null){
            books = bookRepository.findBooksByTitleContainingIgnoreCase(bookName,pageable);
        }
        else{
            books = bookRepository.findAll(pageable);
        }
        return books.map(bookMapper::bookToBookDTO);
    }

    public List<BookDTO> getAllUnborrowedBooks() {
        return bookRepository.findBooksByAvailableCopiesGreaterThan(0).stream().map(bookMapper::bookToBookDTO).toList();
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        book.setAvailableCopies(book.getTotalCopies());
        bookRepository.save(book);
        return bookMapper.bookToBookDTO(book);
    }

    public BookDTO getBookById(UUID bookId){
        Book book =  bookRepository.findById(bookId).orElseThrow(()->  new ResourceNotFoundException("Book With Id Not Found"));
        return bookMapper.bookToBookDTO(book);
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
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
        if (book.getAvailableCopies() <= 0) {
            throw new OperationNotAllowedException("Book Count is already 0, Cannot decrement");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.setTotalCopies(book.getTotalCopies() - 1);
        bookRepository.save(book);
    }

    public void incrementBook(UUID bookId) {
        //incrementing available copies
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        //incrementing total copies
        book.setTotalCopies(book.getTotalCopies() + 1);
        bookRepository.save(book);
    }

    public UploadSummaryDTO uploadBookCSV(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("CSV file must not be empty");

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv"))
            throw new IllegalArgumentException("Only .csv files are accepted");

        //checking if required headers are present
        String firstLine = new String(file.getBytes()).split("\n")[0].toLowerCase().trim();
        if (!firstLine.contains("title") || !firstLine.contains("author") || !firstLine.contains("totalcopies")) {
            throw new IllegalArgumentException("Invalid CSV headers. Expected: title, author, totalCopies");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<BookCSVRepresentation> csvToBean = new CsvToBeanBuilder<BookCSVRepresentation>(reader)
                    .withType(BookCSVRepresentation.class)
                    .withVerifier(new BookBeanVerifier())
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withThrowExceptions(false)
                    .build();

            //name to parsed_csv or more relevent
            List<BookCSVRepresentation> parsed = csvToBean.parse();

            //handling all the collected exceptions
            if (!csvToBean.getCapturedExceptions().isEmpty()) {
                throw new CSVValidationException(csvToBean.getCapturedExceptions());
            }

            int totalRows = parsed.size();
            int totalInserted = 0;

            //  Split the parsed list into batches of 100
            int batchSize = 100;
            for (int i = 0; i < totalRows; i += batchSize) {
                List<BookCSVRepresentation> batch = parsed.subList(i, Math.min(i + batchSize, totalRows));

                // Same logic as before, but only for this batch
                List<String> keys = batch.stream()
                        .map(line -> (line.getTitle() + "|" + line.getAuthor()).toLowerCase())
                        .toList();

                Set<String> existingKeys = bookRepository.findExistingTitleAuthorKeys(keys);

                List<Book> books = batch.stream()
                        .filter(line -> !existingKeys.contains((line.getTitle() + "|" + line.getAuthor()).toLowerCase()))
                        .map(line -> Book.builder()
                                .title(line.getTitle())
                                .author(line.getAuthor())
                                .totalCopies(Integer.parseInt(line.getTotalCopies()))
                                .availableCopies(Integer.parseInt(line.getTotalCopies()))
                                .build())
                        .toList();

                bookRepository.saveAll(books);
                totalInserted += books.size();
            }
            return new UploadSummaryDTO(totalRows, totalInserted, totalRows - totalInserted);
        }
    }

    public List<StudentBookEntry> getAllEntriesByBook(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Student with given id is not available"));
        return book.getStudentBookEntries().stream().toList();
    }

    public void updateBook(BookDTO bookDTO, UUID bookId) {
    }

}
