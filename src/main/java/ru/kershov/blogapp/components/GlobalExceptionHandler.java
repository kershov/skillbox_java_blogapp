package ru.kershov.blogapp.components;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.kershov.blogapp.utils.APIResponse;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    /*
     * Helper to handle POST RequestParams fields validation
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(ConstraintViolationException exception) {
        Map<String, Object> errors = new HashMap<>();

        exception.getConstraintViolations()
                .forEach(constraint -> {
                    PathImpl path = (PathImpl) constraint.getPropertyPath();
                    errors.put(path.getLeafNode().getName(), constraint.getMessage());
                });

        return APIResponse.error(errors);
    }
}