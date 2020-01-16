package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.config.AppProperties;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiGeneralController {
    @Autowired
    private AppProperties appProperties;

    @GetMapping("/api/init")
    public ResponseEntity<Map<String, Object>> getBlogInfo() {
        Map<String, Object> response = new HashMap<>();

        response.put("title", appProperties.getTitle());
        response.put("subtitle", appProperties.getSubtitle());
        response.put("phone", appProperties.getPhone());
        response.put("email", appProperties.getEmail());
        response.put("copyright", appProperties.getCopyright());
        response.put("copyrightFrom", appProperties.getCopyrightFrom());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
