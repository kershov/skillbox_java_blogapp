package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.exceptions.ResponseHandler;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.model.User;
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

    public ResponseEntity<?> registerUser(String email, String name, String password,
                                          String captcha, String captchaSecretCode) {

        Map<String, Object> errors = validateUserInputAndGetErrors(email, name, password, captcha, captchaSecretCode);

        if (errors.size() > 0) {
            return new ResponseHandler().init("")
                    .setStatus(HttpStatus.BAD_REQUEST).setErrors(errors).getResponse();
        }

        // Create and save new user
        User newUser = new User();

        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRegTime(Instant.now());

        usersRepository.save(newUser);

        return new ResponseHandler().init("")
                .setStatus(HttpStatus.OK).setResultOk().getResponse();
    }

    private Map<String, Object> validateUserInputAndGetErrors(String email, String name, String password,
                                                              String captcha, String captchaSecretCode) {
        Map<String, Object> errors = new HashMap<>();

        User user = usersRepository.findByEmail(email);
        CaptchaCode userCaptcha = captchaCodeRepository.findBySecretCode(captchaSecretCode);

        if (user != null) {
            errors.put("email", String.format(Config.STRING_AUTH_EMAIL_ALREADY_REGISTERED, email));
        }

        if (name == null || name.isEmpty()) {
            errors.put("name", Config.STRING_AUTH_WRONG_NAME);
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
