package com.gokul.librarymanagement;

import com.gokul.librarymanagement.model.Book;
import com.gokul.librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SampleTest {

    @Autowired
    private  BookRepository bookRepository;
    
    @Test
    public void testLockPessimistic() {
        Book book = bookRepository.findById(UUID.fromString("8fe45876-cfc0-4df2-9a8f-9cb11a5d0ac3")).get();
        book.setAvailableCopies(9);
        bookRepository.save(book);
    }

    @Test
    public void testLockOptimistic() {

        Book book1 = bookRepository.findById(UUID.fromString("8eabab1f-fee6-47cf-9eb2-0c988366cbab")).get();
        Book book2 = bookRepository.findById(UUID.fromString("8eabab1f-fee6-47cf-9eb2-0c988366cbab")).get();

        book1.setTitle("First Update");
        bookRepository.saveAndFlush(book1);  // flush forces immediate DB write

        book2.setTitle("Second Update");
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            bookRepository.saveAndFlush(book2); // version mismatch — throws!
        });
    }
}
