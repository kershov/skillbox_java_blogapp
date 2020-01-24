package ru.kershov.blogapp.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.kershov.blogapp.enums.GlobalSettings;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "blogapp")
@Data @NoArgsConstructor(force = true)
public class AppProperties {
    private final Map<String, Object> properties = new HashMap<>();
    private final Settings settings = new Settings();

    @Data @NoArgsConstructor(force = true)
    public static class Settings {
        private final Map<String, Object> settings = new HashMap<>();

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
    }
}
