package ru.kershov.blogapp.components;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.kershov.blogapp.config.AppProperties;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TelegramClient {
    private final RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    public TelegramClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Asynchronously sends messages via TgProxy
     *
     * @see ru.kershov.blogapp.config.AsyncConfig#taskExecutor()
     *
     * @param message Message to send. Important: Message has to be properly escaped.
     */
    @Async("taskExecutor")
    public void sendMessage(String message) {
        final boolean enabled = appProperties.getTelegram().isEnabled();
        final String proxyUrl = appProperties.getTelegram().getProxyUrl();
        final String jwtToken = appProperties.getTelegram().getProxyJwtToken();

        if (!enabled || jwtToken.isBlank() || proxyUrl.isBlank())
            return;

        try {
            Map<String, String> payload = new HashMap<>() {{
                put("token", jwtToken);
                put("message", message);
            }};

            ResponseEntity<?> apiResponse = restTemplate.postForEntity(
                    proxyUrl,
                    payload,
                    TelegramApiResponse.class
            );

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                TelegramApiResponse telegramApiResponse = (TelegramApiResponse) apiResponse.getBody();

                if (telegramApiResponse != null) {
                    if (telegramApiResponse.isOk()) {
                        log.info("Message was sent successfully! Response: " + telegramApiResponse.getResult());
                    } else {
                        log.error("Message wasn't sent due to: " + telegramApiResponse.getResult());
                    }
                }

            } else {
                log.error("Message wasn't sent. Response code: " + apiResponse.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error while connecting to Telegram Proxy. Reason: " + e.getMessage());
        }
    }

    @Data
    private static class TelegramApiResponse {
        private boolean ok;
        private Map<String, Object> result;
    }
}
