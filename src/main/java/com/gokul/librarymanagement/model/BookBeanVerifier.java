package com.gokul.librarymanagement.model;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class BookBeanVerifier implements BeanVerifier<BookCSVRepresentation> {
    @Override
    public boolean verifyBean(BookCSVRepresentation bean) throws CsvConstraintViolationException {
        if(bean.getTitle() == null || bean.getTitle().isBlank()){
            throw new CsvConstraintViolationException("Title should be valid");
        }
        if(bean.getAuthor() == null || bean.getAuthor().isBlank()){
            throw new CsvConstraintViolationException("Author should be valid");
        }
        if (bean.getTotalCopies() == null)
            throw new CsvConstraintViolationException("totalCopies should not be null");

        if (bean.getTotalCopies() < 1)
            throw new CsvConstraintViolationException("totalCopies must be at least 1");

        return true;
    }
}
