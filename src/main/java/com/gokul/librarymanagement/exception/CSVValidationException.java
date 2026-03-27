package com.gokul.librarymanagement.exception;

import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CSVValidationException extends RuntimeException {
    List<CsvException> exceptions;
}
