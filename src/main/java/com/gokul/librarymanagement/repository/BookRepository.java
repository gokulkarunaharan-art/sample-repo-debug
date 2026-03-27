package com.gokul.librarymanagement.repository;

import com.gokul.librarymanagement.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findBooksByAvailableCopiesGreaterThan(int numberOfCopiesToFilter);
    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);

    Page<Book> findBooksByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT LOWER(b.title) || '|' || LOWER(b.author) FROM Book b " +
            "WHERE LOWER(b.title) || '|' || LOWER(b.author) IN :keys")
    Set<String> findExistingTitleAuthorKeys(@Param("keys") List<String> keys);
}
