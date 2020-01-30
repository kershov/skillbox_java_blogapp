package ru.kershov.blogapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.model.CaptchaCode;
import ru.kershov.blogapp.repositories.CaptchaCodeRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CaptchaCodeService {
    @Autowired
    AppProperties appProperties;

    @Autowired
    CaptchaCodeRepository captchaCodeRepository;

    public CaptchaCode getCaptcha() {
        final int CODE_TTL = appProperties.getCaptcha().getCodeTTL();
        final int CODE_LENGTH = appProperties.getCaptcha().getCodeLength();
        final int FONT_SIZE = appProperties.getCaptcha().getCodeFontSize();

        // Outdated captchas cleanup
        deleteOutdatedCaptchas(CODE_TTL);

        return captchaCodeRepository.save(new CaptchaCode(CODE_LENGTH, FONT_SIZE));
    }

    private void deleteOutdatedCaptchas(int code_ttl) {
        final Instant TTL = Instant.now().minus(code_ttl, ChronoUnit.HOURS);

        captchaCodeRepository.deleteByTimeBefore(TTL);
    }
}
