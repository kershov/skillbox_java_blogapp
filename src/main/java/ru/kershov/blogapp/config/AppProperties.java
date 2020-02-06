package ru.kershov.blogapp.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.kershov.blogapp.enums.GlobalSettings;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "blogapp") @Validated
@Data @NoArgsConstructor(force = true)
public class AppProperties {
    private final Map<String, Object> properties = new HashMap<>();
    private final Settings settings = new Settings();
    private final Captcha captcha = new Captcha();
    private final Map<String, Integer> sessions = new HashMap<>();

    @Data @NoArgsConstructor(force = true)
    public static class Settings {
        private boolean multiuserMode;
        private boolean postPremoderation;
        private boolean statisticsIsPublic;

        public GlobalSettings.Value isMultiuserMode() {
            return multiuserMode ? GlobalSettings.Value.YES : GlobalSettings.Value.NO;
        }

        public GlobalSettings.Value isPostPremoderation() {
            return postPremoderation ? GlobalSettings.Value.YES : GlobalSettings.Value.NO;
        }

        public GlobalSettings.Value isStatisticsIsPublic() {
            return statisticsIsPublic ? GlobalSettings.Value.YES : GlobalSettings.Value.NO;
        }

        public boolean getMultiuserMode() { return multiuserMode; }

        public boolean getPostPremoderation() { return postPremoderation; }

        public boolean getStatisticsIsPublic() { return statisticsIsPublic; }
    }

    @Data @NoArgsConstructor(force = true)
    public static class Captcha {
        @Min(4) @Max(10)
        private int codeLength;

        @Min(1)
        private int codeTTL;

        @Min(14) @Max(24)
        private int codeFontSize;
    }

    public void addSession(String sessionId, int userId) {
        sessions.put(sessionId, userId);
    }

    public int getUserIdBySessionId(String sessionId) {
        return sessions.getOrDefault(sessionId, null);
    }

    public int deleteSessionById(String sessionId) {
        return sessions.remove(sessionId);
    }
}
