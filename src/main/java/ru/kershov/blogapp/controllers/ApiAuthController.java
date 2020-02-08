package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.model.dto.auth.EmailDTO;
import ru.kershov.blogapp.model.dto.auth.NewUserDTO;
import ru.kershov.blogapp.model.dto.auth.PasswordRestoreDTO;
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

    @PostMapping(value="/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid NewUserDTO user, Errors errors) {
        return userAuthService.registerUser(user, errors);
    }

    @PostMapping(value="/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> loginUser(@RequestBody @Valid UnauthorizedUserDTO user, Errors errors) {
        return userAuthService.loginUser(user, errors);
    }

    @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logoutUser() {
        return userAuthService.logoutUser();
    }

    @GetMapping(value="/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkUserIsAuthorized() {
        return userAuthService.checkUserIsAuthorized();
    }

    @PostMapping(value = "/restore",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restoreUserPassword(@RequestBody @Valid EmailDTO email, Errors errors) {
        return userAuthService.restoreUserPassword(email, errors);
    }

    @PostMapping(value = "/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetUserPassword(@RequestBody @Valid PasswordRestoreDTO request,
                                               Errors errors) {
        return userAuthService.resetUserPassword(request, errors);
    }

    @GetMapping(value="/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<?> getCaptcha() {
        return captchaCodeService.getCaptcha();
    }
}
