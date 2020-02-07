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
    private Map<String, Object> payload = new HashMap<>();
    private Map<String, Object> errors = new HashMap<>();

    @JsonIgnore
    private boolean result = false;

    public ResponseHandler init(String message) {
        payload.put("timestamp", Timestamp.valueOf(
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));

        payload.put("result", result);

        if (!message.isEmpty()) payload.put("message", message);

        return this;
    }

    public ResponseHandler setResultOk() {
        this.result = true;
        return this;
    }

    public ResponseHandler setResultOk(String key, Object value) {
        payload.put("result", true);
        payload.put(key, value);
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

    public Map<String, Object> getErrors() {
        return errors;
    }

    public ResponseHandler setStatus(HttpStatus status) {
        this.status = status;
        payload.put("status", this.status.value());
        if (status != HttpStatus.OK) payload.put("error", this.status.getReasonPhrase());
        return this;
    }

    public Map<String, Object> getPayload() {
        if (!errors.isEmpty()) payload.put("errors", errors);
        return payload;
    }

    public ResponseEntity<?> getResponse() {
        if (result) {
            payload = new HashMap<>();
            payload.put("result", this.result);
        }

        return ResponseEntity.status(status).body(getPayload());
    }
}
