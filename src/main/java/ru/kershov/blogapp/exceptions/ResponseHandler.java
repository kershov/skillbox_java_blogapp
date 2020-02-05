package ru.kershov.blogapp.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private HttpStatus status;
    private Map<String, Object> errorMessage = new HashMap<>();
    private Map<String, Object> errors = new HashMap<>();

    @JsonIgnore
    private boolean result = false;

    public ResponseHandler init(String message) {
        errorMessage.put("timestamp", Timestamp.valueOf(
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));

        errorMessage.put("result", result);

        if (!message.isEmpty()) errorMessage.put("message", message);

        return this;
    }

    public ResponseHandler setResultOk() {
        this.result = true;
        return this;
    }

    public ResponseHandler setError(String key, String value) {
        errors.put(key, value);
        return this;
    }

    public ResponseHandler setErrors(Map<String, Object> errors) {
        this.errors = errors;
        return this;
    }

    public ResponseHandler setStatus(HttpStatus status) {
        this.status = status;
        errorMessage.put("status", this.status.value());
        errorMessage.put("error", this.status.getReasonPhrase());
        return this;
    }

    public ResponseEntity<?> getResponse() {
        if (!errors.isEmpty()) errorMessage.put("errors", errors);

        if (result) {
            errorMessage = new HashMap<>();
            errorMessage.put("result", this.result);
        }

        return ResponseEntity.status(status).body(errorMessage);
    }
}
