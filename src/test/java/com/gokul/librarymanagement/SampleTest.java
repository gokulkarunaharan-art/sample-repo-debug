package com.gokul.librarymanagement;

import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class SampleTest {

    @Autowired
    private  BookRepository bookRepository;


    @Test
    public void testLockPessimistic(){
        Book book = bookRepository.findById(UUID.fromString("8fe45876-cfc0-4df2-9a8f-9cb11a5d0ac3")).get();
        book.setAvailableCopies(9);
        bookRepository.save(book);
    }

    @Test
    public void testLockOptimistic(){

    }
}
