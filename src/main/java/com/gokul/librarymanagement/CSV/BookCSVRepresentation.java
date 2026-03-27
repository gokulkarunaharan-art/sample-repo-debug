package com.gokul.librarymanagement.CSV;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCSVRepresentation {
    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "author")
    private String author;

    @CsvBindByName(column = "totalCopies")
    private String totalCopies;
}
