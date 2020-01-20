package ru.kershov.blogapp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.kershov.blogapp.enums.GlobalSettings;

@Configuration("AppSettings")
@ConfigurationProperties(prefix = "blogapp.settings")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor
public class AppSettings {
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
