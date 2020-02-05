package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.exceptions.ResponseHandler;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.NewUserDTO;
import ru.kershov.blogapp.repositories.CaptchaCodeRepository;
import ru.kershov.blogapp.repositories.UsersRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterUserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CaptchaCodeRepository captchaCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder BCryptEncoder() {
        return new BCryptPasswordEncoder(Config.INT_AUTH_BCRYPT_STRENGTH);
    }

    public ResponseEntity<?> registerUser(NewUserDTO user, Errors validationErrors) {
        Map<String, Object> errors = validateUserInputAndGetErrors(user, validationErrors);

        if (errors.size() > 0) {
            return new ResponseHandler().init("Registration Error")
                    .setStatus(HttpStatus.BAD_REQUEST).setErrors(errors).getResponse();
        }

        // Create and save new user
        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRegTime(Instant.now());

        usersRepository.save(newUser);

        return new ResponseHandler().init("")
                .setStatus(HttpStatus.OK).setResultOk().getResponse();
    }

    private Map<String, Object> validateUserInputAndGetErrors(NewUserDTO user, Errors validationErrors) {
        final String email = user.getEmail();

        final String password = user.getPassword();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();

        if (validationErrors.hasErrors()) {
            validationErrors.getFieldErrors().forEach(
                    err -> errors.put(err.getField(), err.getDefaultMessage())
            );
            return errors;
        }

        User userFromDB = usersRepository.findByEmail(email);
        CaptchaCode userCaptcha = captchaCodeRepository.findBySecretCode(captchaSecretCode);

        if (userFromDB != null) {
            errors.put("email", String.format(Config.STRING_AUTH_EMAIL_ALREADY_REGISTERED, email));
        }

        if (password == null || password.length() < Config.INT_AUTH_MIN_PASSWORD_LENGTH) {
            errors.put("password", Config.STRING_AUTH_INVALID_PASSWORD_LENGTH);
        }

        if (userCaptcha == null || !userCaptcha.isValidCode(captcha)) {
            errors.put("captcha", Config.STRING_AUTH_INVALID_CAPTCHA);
        }
        return errors;
    }
}
