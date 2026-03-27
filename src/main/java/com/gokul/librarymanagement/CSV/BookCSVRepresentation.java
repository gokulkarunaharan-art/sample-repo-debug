package com.gokul.librarymanagement.CSV;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCSVRepresentation {
    @CsvBindByName(column = "title", required = true)
    private String title;

    @CsvBindByName(column = "author", required = true)
    private String author;

    @CsvBindByName(column = "totalCopies", required = true)
    private String totalCopies;
}
