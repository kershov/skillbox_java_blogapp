package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.SettingsDTO;
import ru.kershov.blogapp.services.SettingsService;
import ru.kershov.blogapp.services.StatsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.APIResponse;
import ru.kershov.blogapp.utils.JsonViews;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/init")
    public ResponseEntity<Map<String, Object>> getBlogInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(appProperties.getProperties());
    }

    @GetMapping(value = "/statistics/{statsType:(?:all|my)}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getStats(@PathVariable(value = "statsType") String statsType) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();
        boolean isStatsPublic = settingsService.isStatsPublic();

        // User is unauthorized >> show authorized user stats
        if (userOptional.isPresent()) {
            if (statsType.equalsIgnoreCase("all") && isStatsPublic)
                return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(null));

            return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(userOptional.get()));
        }

        // Unauthorized + stats is public >> show public stats
        if (isStatsPublic) {
            return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(null));
        }

        // Unauthorized + stats is private >> 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());
    }

    @GetMapping("/settings")
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getSettings() {
        // TODO: Temporary disabled due to some bug in vue.js front app
//        if (!userAuthService.isAuthorizedAndModerator()) {
//            return ResponseEntity.ok(new HashMap<>() {{
//                put(GlobalSettings.Code.STATISTICS_IS_PUBLIC, settingsService.getSettings().isStatisticsIsPublic());
//            }});
//        }

        return ResponseEntity.ok(settingsService.getSettings());
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateSettings(@RequestBody @Valid SettingsDTO settings) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        if (!user.isModerator())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.error());

        SettingsDTO newSettings = settingsService.saveSettings(settings);
        return ResponseEntity.ok(newSettings);
    }
}
