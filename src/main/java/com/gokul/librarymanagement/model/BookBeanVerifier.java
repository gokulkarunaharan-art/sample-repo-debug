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
        if(bean.getTotalCopies() < 0){
            throw new CsvConstraintViolationException("total copies should be positive and valid");
        }
        return true;
    }
}
