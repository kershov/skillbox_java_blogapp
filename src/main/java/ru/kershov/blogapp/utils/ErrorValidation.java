package ru.kershov.blogapp.utils;

import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

public class ErrorValidation {
    public static Map<String, Object> getValidationErrors(Errors validationErrors) {
        Map<String, Object> errors = new HashMap<>();
        validationErrors.getFieldErrors().forEach(
                err -> errors.put(err.getField(), err.getDefaultMessage())
        );
        return errors;
    }
}
