package ru.kershov.blogapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.exceptions.ResponseHandler;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.auth.AuthorizedUserDTO;
import ru.kershov.blogapp.model.dto.auth.NewUserDTO;
import ru.kershov.blogapp.model.dto.auth.UnauthorizedUserDTO;
import ru.kershov.blogapp.repositories.CaptchaCodeRepository;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.UsersRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserAuthService {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsRepository postsRepository;

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
            return new ResponseHandler().init(Config.STRING_AUTH_REGISTRATION_ERROR)
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

    public ResponseEntity<?> loginUser(UnauthorizedUserDTO user, Errors validationErrors) {
        final String email = user.getEmail();
        final String password = user.getPassword();

        if (validationErrors.hasErrors()) {
            log.info("Field validation errors.");
            return new ResponseHandler().init(Config.STRING_AUTH_ERROR)
                    .setErrors(getValidationErrors(validationErrors))
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .getResponse();
        }

        if (email.isBlank() || password.isBlank()) {
            log.info("Either email or password are empty.");
            return new ResponseHandler().init(Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .getResponse();
        }

        log.info(String.format("Trying to authenticate user with email '%s' " +
                "and password '***'.", email));

        User userFromDB = usersRepository.findByEmail(email);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return new ResponseHandler().init(Config.STRING_AUTH_ERROR)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setError("email", String.format(Config.STRING_AUTH_LOGIN_NO_SUCH_USER, email))
                    .getResponse();
        }

        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        // Validate user's password
        if ( !isValidPassword(password, userFromDB.getPassword()) ) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return new ResponseHandler().init(Config.STRING_AUTH_ERROR)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setError("password", Config.STRING_AUTH_WRONG_PASSWORD)
                    .getResponse();
        }

        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        final int userId = userFromDB.getId();
        appProperties.addSession(sessionId, userId);

        log.info(String.format("User with email '%s' successfully authenticated with sessionId: %s",
                email, sessionId));

        AuthorizedUserDTO authorizedUser = getAuthorizedUser(userFromDB);

        return new ResponseHandler().init(Config.STRING_AUTH_SUCCESSFULLY_AUTHORIZED)
                .setStatus(HttpStatus.OK).setResultOk("user", authorizedUser).getResponse();
    }

    public ResponseEntity<?> logoutUser() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        if (appProperties.getSessions().containsKey(sessionId)) {
            int userId = appProperties.deleteSessionById(sessionId);
            User userFromDB = usersRepository.findById(userId).orElse(null);

            log.info(String.format("Session '%s' for user '%s' successfully deleted.",
                    sessionId, userFromDB));
        } else {
            log.info(String.format("Session '%s' not found.", sessionId));
        }

        return new ResponseHandler().init("")
                .setStatus(HttpStatus.OK).setResultOk().getResponse();
    }

    /*** Various Helpers ***/

    private Map<String, Object> validateUserInputAndGetErrors(NewUserDTO user, Errors validationErrors) {
        final String email = user.getEmail();

        final String password = user.getPassword();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();

        if (validationErrors.hasErrors()) {
            return getValidationErrors(validationErrors);
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

    private Map<String, Object> getValidationErrors(Errors validationErrors) {
        Map<String, Object> errors = new HashMap<>();
        validationErrors.getFieldErrors().forEach(
                err -> errors.put(err.getField(), err.getDefaultMessage())
        );
        return errors;
    }

    private AuthorizedUserDTO getAuthorizedUser(User user) {
        AuthorizedUserDTO authorizedUser = new AuthorizedUserDTO();

        authorizedUser.setId(user.getId());
        authorizedUser.setName(user.getName());
        authorizedUser.setPhoto(user.getPhoto());
        authorizedUser.setEmail(user.getEmail());

        if (user.isModerator()) {
            authorizedUser.setUserIsModerator(
                    postsRepository.countByModeratedByAndModerationStatus(user, ModerationStatus.NEW)
            );
        }

        return authorizedUser;
    }

    private boolean isValidPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
