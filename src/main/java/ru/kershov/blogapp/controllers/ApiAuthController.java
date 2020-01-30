package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.services.CaptchaCodeService;
import ru.kershov.blogapp.utils.JsonViews;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    CaptchaCodeService captchaCodeService;

    @GetMapping(value="/captcha", produces = "application/json")
    @JsonView(JsonViews.Name.class)
    public ResponseEntity<CaptchaCode> getCaptcha() {
        return ResponseEntity.status(HttpStatus.OK).body(captchaCodeService.getCaptcha());
    }
}
