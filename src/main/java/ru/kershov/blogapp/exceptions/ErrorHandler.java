package ru.kershov.blogapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {
    private HttpStatus status;
    private Map<String, Object> errorMessage = new HashMap<>();
    private Map<String, Object> errors = new HashMap<>();

    public ErrorHandler init(String message) {
        errorMessage.put("timestamp", Timestamp.valueOf(
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));

        errorMessage.put("result", false);
        errorMessage.put("message", message);

        return this;
    }

    public ErrorHandler setError(String key, String value) {
        errors.put(key, value);
        return this;
    }

    public ErrorHandler setErrors(Map<String, Object> errors) {
        this.errors = errors;
        return this;
    }

    public ErrorHandler setStatus(HttpStatus status) {
        this.status = status;
        errorMessage.put("status", this.status.value());
        errorMessage.put("error", this.status.getReasonPhrase());
        return this;
    }

    public ResponseEntity<?> getErrorResponse() {
        if (!errors.isEmpty()) errorMessage.put("errors", errors);
        return ResponseEntity.status(status).body(errorMessage);
    }
}
