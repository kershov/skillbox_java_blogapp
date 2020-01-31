package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.model.dto.SettingsDTO;
import ru.kershov.blogapp.model.dto.StatsDTO;
import ru.kershov.blogapp.services.SettingsService;
import ru.kershov.blogapp.services.StatsService;
import ru.kershov.blogapp.utils.JsonViews;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private StatsService statsService;

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/init")
    public ResponseEntity<Map<String, Object>> getBlogInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(appProperties.getProperties());
    }

    @GetMapping(value = {"/statistics/all", "/statistics/my"})
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<StatsDTO> getStats() {
        // TODO: If GlobalSettings.STATISTICS_IS_PUBLIC = false && User is UNAUTHORIZED >>> 401
        //       If Authorized >>> Statistics is shown only for authorized User
        //       To be specified!!!
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats());
    }

    @GetMapping("/settings")
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<SettingsDTO> getSettings() {
        // TODO: If User is UNAUTHORIZED >>> 401
        return ResponseEntity.status(HttpStatus.OK).body(settingsService.getSettings());
    }
}
