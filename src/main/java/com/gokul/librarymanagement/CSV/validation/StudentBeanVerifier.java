package com.gokul.librarymanagement.CSV.validation;

import com.gokul.librarymanagement.CSV.StudentCSVRepresentation;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;

public class StudentBeanVerifier implements BeanVerifier<StudentCSVRepresentation> {
    @Override
    public boolean verifyBean(StudentCSVRepresentation line) throws CsvConstraintViolationException {
        if (StringUtils.isBlank(line.getName()) || GenericValidator.isInt(line.getName())) {
            throw new CsvConstraintViolationException(" INVALID CSV: Name Should be valid ");
        }
        if (StringUtils.isBlank(line.getEmail()) || !EmailValidator.getInstance().isValid(line.getEmail())) {
            throw new CsvConstraintViolationException(" INVALID CSV: Email should be valid ");
        }

        String phone = line.getPhoneNumber().replace("+", "").trim();
        if (StringUtils.isBlank(phone) || !StringUtils.isNumeric(phone))
            throw new CsvConstraintViolationException(" INVALID CSV: phoneNumber should be valid ");
        return true;
    }
}