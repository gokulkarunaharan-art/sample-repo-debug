package com.gokul.librarymanagement.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Table(name = "books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String author;

    private int totalCopies;

    private int availableCopies;
}
