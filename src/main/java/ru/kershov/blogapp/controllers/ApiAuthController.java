package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.model.dto.auth.NewUserDTO;
import ru.kershov.blogapp.model.dto.auth.UnauthorizedUserDTO;
import ru.kershov.blogapp.services.CaptchaCodeService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.JsonViews;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private CaptchaCodeService captchaCodeService;

    @PostMapping(value="/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody @Valid NewUserDTO user, Errors errors) {
        return userAuthService.registerUser(user, errors);
    }

    @PostMapping(value="/login", consumes = "application/json", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> loginUser(@RequestBody @Valid UnauthorizedUserDTO user, Errors errors) {
        return userAuthService.loginUser(user, errors);
    }

    @GetMapping(value="/logout", produces = "application/json")
    public ResponseEntity<?> logoutUser() {
        return userAuthService.logoutUser();
    }

    @GetMapping(value="/captcha", produces = "application/json")
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getCaptcha() {
        return captchaCodeService.getCaptcha();
    }
}
