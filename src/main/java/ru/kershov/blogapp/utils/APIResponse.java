package ru.kershov.blogapp.utils;

import java.util.HashMap;
import java.util.Map;

public class APIResponse {
    private static APIResponse.Builder getResult(boolean result) {
        return new APIResponse.Builder(result);
    }

    public static Map<String, Object> ok() {
        return getResult(true).build();
    }

    public static Map<String, Object> ok(String message) {
        return getResult(true).setMessage(message).build();
    }

    public static Map<String, Object> ok(String key, Object payload) {
        return getResult(true).addPayload(key, payload).build();
    }

    public static Map<String, Object> error() {
        return getResult(false).build();
    }

    public static Map<String, Object> error(String message) {
        return getResult(false).setMessage(message).build();
    }

    public static Map<String, Object> error(Map<String, Object> errors) {
        return getResult(false).setErrors(errors).build();
    }

    public static Map<String, Object> error(String message, Map<String, Object> errors) {
        return getResult(false).setMessage(message).setErrors(errors).build();
    }

    private static class Builder {
        private final Map<String, Object> payload = new HashMap<>();

        public Builder(boolean result) {
            payload.put("result", result);
        }

        public Builder setMessage(String message) {
            this.payload.put("message", message);
            return this;
        }

        public Builder setErrors(Map<String, Object> errors) {
            this.payload.put("errors", errors);
            return this;
        }

        public Builder addPayload(String key, Object payload) {
            this.payload.put(key, payload);
            return this;
        }

        public Map<String, Object> build() {
            return payload;
        }
    }
}
