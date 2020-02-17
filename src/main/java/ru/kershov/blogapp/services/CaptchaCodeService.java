package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.repositories.CaptchaCodeRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class CaptchaCodeService {
    @Autowired
    AppProperties appProperties;

    @Autowired
    CaptchaCodeRepository captchaCodeRepository;

    public ResponseEntity<?> getCaptcha() {
        final int CODE_TTL = appProperties.getCaptcha().getCodeTTL();
        final int CODE_LENGTH = appProperties.getCaptcha().getCodeLength();
        final int FONT_SIZE = appProperties.getCaptcha().getCodeFontSize();

        // Outdated captchas cleanup
        deleteOutdatedCaptchas(CODE_TTL);

        return ResponseEntity.status(HttpStatus.OK).body(
                captchaCodeRepository.save(new CaptchaCode(CODE_LENGTH, FONT_SIZE))
        );
    }

    private void deleteOutdatedCaptchas(int code_ttl) {
        final Instant TTL = Instant.now().minus(code_ttl, ChronoUnit.HOURS);

        captchaCodeRepository.deleteByTimeBefore(TTL);
    }

    public boolean isValidCaptcha(String userCaptcha, String userCaptchaSecretCode) {
        Optional<CaptchaCode> captchaOpt = Optional.ofNullable(
                captchaCodeRepository.findBySecretCode(userCaptchaSecretCode)
        );

        return captchaOpt.isPresent() && captchaOpt.get().isValidCode(userCaptcha);
    }
}
