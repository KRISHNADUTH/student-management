package com.ghss.studentmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourseAlreadyExistsException extends RuntimeException {
    public ResourseAlreadyExistsException(String resource, String fieldName, String fieldValue) {
        super(String.format("%s with %s = '%s' already present", resource, fieldName, fieldValue));
    }
}
