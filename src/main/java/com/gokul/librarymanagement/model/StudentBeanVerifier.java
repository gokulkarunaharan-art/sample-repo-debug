package com.gokul.librarymanagement.model;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class StudentBeanVerifier implements BeanVerifier<StudentCSVRepresentation> {
    @Override
    public boolean verifyBean(StudentCSVRepresentation line) throws CsvConstraintViolationException {
        return true;
    }
}
