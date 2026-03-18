package com.gokul.librarymanagement.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private String title;

    private String author;

    private int totalCopies;

    private int availableCopies;
}
