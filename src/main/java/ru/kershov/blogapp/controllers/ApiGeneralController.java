package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.config.AppProperties;

import java.util.Map;

@RestController
public class ApiGeneralController {
    @Autowired
    private AppProperties appProperties;

    @GetMapping("/api/init")
    public ResponseEntity<Map<String, Object>> getBlogInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(appProperties.getProperties());
    }
}
