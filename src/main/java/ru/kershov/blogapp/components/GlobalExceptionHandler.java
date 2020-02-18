package ru.kershov.blogapp.components;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.services.FileSystemStorageService;
import ru.kershov.blogapp.utils.APIResponse;
import ru.kershov.blogapp.utils.ErrorValidation;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {
    /** Helper to handle POST RequestParams fields validation */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> errors = new HashMap<>();

        e.getConstraintViolations()
                .forEach(constraint -> {
                    PathImpl path = (PathImpl) constraint.getPropertyPath();
                    errors.put(path.getLeafNode().getName(), constraint.getMessage());
                });

        return APIResponse.error(Config.STRING_VALIDATION_MESSAGE, errors);
    }

    /**
     * Invalid request body arguments handler
     *
     * TODO: Specify what HttpStatus should be returned: 400 or 200?
     *       Code 400 isn't handled properly by frontend app while 200
     *       is handled OK at /login/registration
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = ErrorValidation.getValidationErrors(e.getBindingResult());

        return APIResponse.error(Config.STRING_VALIDATION_MESSAGE, errors);
    }

    /** Empty request body */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return APIResponse.error(Config.STRING_VALIDATION_MESSAGE);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleStorageExceptions(FileSystemStorageService.StorageException e) {
        return APIResponse.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleStorageFileNotFound(FileSystemStorageService.StorageFileNotFoundException e) {
        return APIResponse.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return APIResponse.error(new HashMap<>() {{
            put(e.getName(), String.format(Config.STRING_ERROR_HANDLER_INVALID_OPTION,
                    e.getName(), e.getValue()));
        }});
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return APIResponse.error(new HashMap<>() {{
            put(e.getParameterName(), e.getMessage());
        }});
    }
}