package com.gokul.librarymanagement.CSV.validation;

import com.gokul.librarymanagement.CSV.BookCSVRepresentation;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

public class BookBeanVerifier implements BeanVerifier<BookCSVRepresentation> {
    @Override
    public boolean verifyBean(BookCSVRepresentation bean) throws CsvConstraintViolationException {
        if (StringUtils.isBlank(bean.getTitle()) || GenericValidator.isInt(bean.getTitle())) {
            throw new CsvConstraintViolationException(" INVALID CSV: Title should be valid ");
        }

        if (StringUtils.isBlank(bean.getAuthor()) || GenericValidator.isInt(bean.getAuthor())) {
            throw new CsvConstraintViolationException(" INVALID CSV: Author should be valid ");
        }

        if (StringUtils.isBlank(bean.getTotalCopies()) || !GenericValidator.isInt(bean.getTotalCopies()))
            throw new CsvConstraintViolationException(" INVALID CSV: totalCopies should be valid ");

        if (Integer.parseInt(bean.getTotalCopies()) < 1)
            throw new CsvConstraintViolationException(" INVALID CSV: totalCopies must be at least 1 ");

        return true;
    }
}
