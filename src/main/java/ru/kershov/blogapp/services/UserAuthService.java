package ru.kershov.blogapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import ru.kershov.blogapp.components.TelegramClient;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.auth.*;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.UsersRepository;
import ru.kershov.blogapp.utils.APIResponse;
import ru.kershov.blogapp.utils.ErrorValidation;
import ru.kershov.blogapp.utils.StringUtils;

import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserAuthService {
    @Autowired
    private Environment environment;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CaptchaCodeService captchaCodeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TelegramClient telegramClient;

    @Bean
    public PasswordEncoder BCryptEncoder() {
        return new BCryptPasswordEncoder(Config.INT_AUTH_BCRYPT_STRENGTH);
    }

    public ResponseEntity<?> registerUser(NewUserDTO user, Errors validationErrors) {
        Map<String, Object> errors = validateUserInputAndGetErrors(user, validationErrors);

        if (errors.size() > 0)
            return ResponseEntity.ok(APIResponse.error(errors));

        // Create and save new user
        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRegTime(Instant.now());

        usersRepository.save(newUser);

        telegramClient.sendMessage(String.format(Config.STRING_TELEGRAM_USER_REGISTERED,
                StringUtils.escapeString(user.getEmail())
        ));

        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> loginUser(UnauthorizedUserDTO user, Errors validationErrors) {
        if (validationErrors.hasErrors())
            return ResponseEntity.badRequest().body(APIResponse.error(Config.STRING_AUTH_ERROR));

        final String email = user.getEmail();
        final String password = user.getPassword();

        if (email.isBlank() || password.isBlank())
            return ResponseEntity.badRequest().body(APIResponse.error(
                    Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD));

        log.info(String.format("Trying to authenticate user with email '%s' " +
                "and password '***'.", email));

        User userFromDB = usersRepository.findByEmail(email);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return ResponseEntity.ok(APIResponse.error(Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }

        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        // Validate user's password
        if (!isValidPassword(password, userFromDB.getPassword())) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity.badRequest().body(APIResponse.error(Config.STRING_AUTH_WRONG_PASSWORD));
        }

        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        appProperties.addSession(sessionId, userFromDB.getId());

        log.info(String.format("User with email '%s' successfully authenticated with sessionId: %s",
                userFromDB.getEmail(), sessionId));

        AuthorizedUserDTO authorizedUser = getAuthorizedUserDTO(userFromDB);

        return ResponseEntity.ok(APIResponse.ok("user", authorizedUser));
    }

    public ResponseEntity<?> logoutUser() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        if (isAuthorized()) {
            appProperties.deleteSessionById(sessionId);
        }

        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> checkUserIsAuthorized() {
        Optional<User> userOptional = getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.ok(APIResponse.error());

        User user = userOptional.get();

        log.info(String.format("User '%s' is authenticated.", user));

        AuthorizedUserDTO authorizedUser = getAuthorizedUserDTO(user);

        return ResponseEntity.ok(APIResponse.ok("user", authorizedUser));
    }

    public ResponseEntity<?> restoreUserPassword(EmailDTO email, Errors validationErrors) {
        if (validationErrors.hasErrors())
            return ResponseEntity.ok(
                    APIResponse.error(ErrorValidation.getValidationErrors(validationErrors))
            );

        final String userEmail = email.getEmail();
        User userFromDB = usersRepository.findByEmail(userEmail);

        if (userFromDB == null)
            return ResponseEntity.ok(APIResponse.error(Config.STRING_AUTH_LOGIN_NO_SUCH_USER));

        log.info(String.format("User with email '%s' found: %s", userEmail, userFromDB));

        final String code = UUID.randomUUID().toString();

        userFromDB.setCode(code);
        User updatedUser = usersRepository.save(userFromDB);

        final String port = environment.getProperty("server.port");
        final String hostName = InetAddress.getLoopbackAddress().getHostName();
        final String url = String.format(Config.STRING_AUTH_SERVER_URL, hostName, port);

        mailSenderService.send(
                updatedUser.getEmail(),
                Config.STRING_AUTH_MAIL_SUBJECT,
                String.format(Config.STRING_AUTH_MAIL_MESSAGE, url, code)
        );

        return ResponseEntity.ok(APIResponse.ok());
    }

    public ResponseEntity<?> resetUserPassword(PasswordRestoreDTO request, Errors validationErrors) {
        if (validationErrors.hasErrors())
            return ResponseEntity.ok(
                    APIResponse.error(ErrorValidation.getValidationErrors(validationErrors))
            );

        final Map<String, Object> errors = new HashMap<>();

        if (!captchaCodeService.isValidCaptcha(request.getCaptcha(), request.getCaptchaSecret()))
            errors.put("captcha", Config.STRING_AUTH_INVALID_CAPTCHA);

        if (request.getPassword().length() < Config.INT_AUTH_MIN_PASSWORD_LENGTH)
            errors.put("password", Config.STRING_AUTH_INVALID_PASSWORD_LENGTH);

        if (!errors.isEmpty())
            return ResponseEntity.ok(APIResponse.error(errors));

        User userFromDB = usersRepository.findByCode(request.getCode());

        if (userFromDB == null) {
            errors.put("code", Config.STRING_AUTH_CODE_IS_OUTDATED);
            return ResponseEntity.ok(APIResponse.error(errors));
        }

        userFromDB.setCode(null);
        userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
        usersRepository.save(userFromDB);

        return ResponseEntity.ok(APIResponse.ok());
    }

    /*** Various Helpers ***/

    private Map<String, Object> validateUserInputAndGetErrors(NewUserDTO user, Errors validationErrors) {
        final String email = user.getEmail();

        final String password = user.getPassword();
        final String captcha = user.getCaptcha();
        final String captchaSecretCode = user.getCaptchaSecret();

        Map<String, Object> errors = new HashMap<>();

        if (validationErrors.hasErrors())
            return ErrorValidation.getValidationErrors(validationErrors);

        User userFromDB = usersRepository.findByEmail(email);

        if (userFromDB != null)
            errors.put("email", Config.STRING_AUTH_EMAIL_ALREADY_REGISTERED);

        if (password == null || password.length() < Config.INT_AUTH_MIN_PASSWORD_LENGTH)
            errors.put("password", Config.STRING_AUTH_INVALID_PASSWORD_LENGTH);

        if (!captchaCodeService.isValidCaptcha(captcha, captchaSecretCode))
            errors.put("captcha", Config.STRING_AUTH_INVALID_CAPTCHA);

        return errors;
    }

    private AuthorizedUserDTO getAuthorizedUserDTO(User user) {
        AuthorizedUserDTO authorizedUser = new AuthorizedUserDTO();

        authorizedUser.setId(user.getId());
        authorizedUser.setName(user.getName());
        authorizedUser.setPhoto(user.getPhoto());
        authorizedUser.setEmail(user.getEmail());

        if (user.isModerator()) {
            authorizedUser.setUserIsModerator(
                    postsRepository.countPostAwaitingModeration()
            );
        }

        return authorizedUser;
    }

    private boolean isValidPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    public boolean isAuthorized() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        if (!appProperties.getSessions().containsKey(sessionId)) {
            log.info(String.format("Unauthorized user. Session '%s' not found.", sessionId));
            return false;
        }

        log.info(String.format("Authorized user. Session '%s' found.", sessionId));
        return true;
    }

    public Optional<User> getAuthorizedUser() {
        if (isAuthorized()) {
            final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            int userId = appProperties.getUserIdBySessionId(sessionId);

            return usersRepository.findById(userId);
        }

        return Optional.empty();
    }

    public boolean isModerator(User user) {
        return user.isModerator();
    }
}
