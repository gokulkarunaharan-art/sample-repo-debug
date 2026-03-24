package com.gokul.librarymanagement.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String author;

    private Integer totalCopies;

    private Integer availableCopies;
    @OneToMany(mappedBy = "book")

    private Set<StudentBookEntry> studentBookEntries;

}
