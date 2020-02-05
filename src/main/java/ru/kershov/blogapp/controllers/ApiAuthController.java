package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.model.dto.NewUserDTO;
import ru.kershov.blogapp.services.CaptchaCodeService;
import ru.kershov.blogapp.services.RegisterUserService;
import ru.kershov.blogapp.utils.JsonViews;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private CaptchaCodeService captchaCodeService;

    @PostMapping(value="/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody @Valid NewUserDTO user, Errors errors) {
        return registerUserService.registerUser(user, errors);
    }

    @GetMapping(value="/captcha", produces = "application/json")
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getCaptcha() {
        return captchaCodeService.getCaptcha();
    }
}
