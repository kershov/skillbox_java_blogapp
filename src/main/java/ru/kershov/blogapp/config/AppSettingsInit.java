package ru.kershov.blogapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kershov.blogapp.BlogAppApplication;
import ru.kershov.blogapp.enums.GlobalSettings;
import ru.kershov.blogapp.model.Settings;
import ru.kershov.blogapp.repositories.SettingsRepository;

public class AppSettingsInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogAppApplication.class);

    public static void init(AppSettings appSettings, SettingsRepository settingsRepository) {
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

            Settings result = settingsRepository.save(option);
            LOGGER.info(String.format("Option '%s' is set to '%s'", code, value));
        }
    }
}
