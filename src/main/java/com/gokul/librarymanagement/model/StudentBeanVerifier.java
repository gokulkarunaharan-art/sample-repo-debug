package com.gokul.librarymanagement.model;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class StudentBeanVerifier implements BeanVerifier<StudentCSVRepresentation> {
    @Override
    public boolean verifyBean(StudentCSVRepresentation line) throws CsvConstraintViolationException {
        if(line.getName() == null || line.getName().isBlank()){
            throw new CsvConstraintViolationException("-->Invalid CSV<--Name Should be valid");
        }
        if(line.getEmail() == null || line.getEmail().isBlank()){
            throw new CsvConstraintViolationException("-->Invalid CSV<--Email should be valid");
        }
        if (line.getPhoneNumber() == null || line.getPhoneNumber().isBlank())
            throw new CsvConstraintViolationException("-->Invalid CSV<--phoneNumber should not be blank");
        return true;
    }
}
