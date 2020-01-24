package ru.kershov.blogapp.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.kershov.blogapp.config.AppProperties;
import ru.kershov.blogapp.enums.GlobalSettings;
import ru.kershov.blogapp.model.Settings;
import ru.kershov.blogapp.repositories.SettingsRepository;

@Slf4j
@Component("db-settings-init-runner")
@ConditionalOnExpression("${blogapp.runners.db-settings-init-enabled:false}")
public class DBSettingsInitRunner implements CommandLineRunner {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void run(String... args) throws Exception {
        // TODO: Temporary. To be refactored. Check if settings persist in the DB >> Do nothing if so

        final AppProperties.Settings appSettings = appProperties.getSettings();

        for (GlobalSettings.Code code : GlobalSettings.Code.values()) {
            GlobalSettings.Value value = null;

            switch (code) {
                case MULTIUSER_MODE:
                    value = appSettings.isMultiuserMode();
                    break;
                case POST_PREMODERATION:
                    value = appSettings.isPostPremoderation();
                    break;
                case STATISTICS_IS_PUBLIC:
                    value = appSettings.isStatisticsIsPublic();
                    break;
                default:
                    break;
            }

            Settings option = new Settings();

            option.setCode(code);
            option.setName(code.getName());
            option.setValue(value);

            settingsRepository.save(option);
            log.info(String.format("Option '%s' is set to '%s'", code, value));
        }
    }
}
