package com.ghss.studentmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String fieldName, String fieldValue) {
        super(String.format("%s with %s = '%s' not available", resource, fieldName, fieldValue));
    }
}
