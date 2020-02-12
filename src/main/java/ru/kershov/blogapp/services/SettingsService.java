package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.enums.GlobalSettings;
import ru.kershov.blogapp.model.Settings;
import ru.kershov.blogapp.model.dto.SettingsDTO;
import ru.kershov.blogapp.repositories.SettingsRepository;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    public SettingsDTO getSettings() {
        SettingsDTO settings = new SettingsDTO();

        settingsRepository.findAll().forEach(setting -> {
            final var MULTIUSER_MODE = settingsRepository.findByCodeIs(GlobalSettings.Code.MULTIUSER_MODE);
            final var POST_PREMODERATION = settingsRepository.findByCodeIs(GlobalSettings.Code.POST_PREMODERATION);
            final var STATISTICS_IS_PUBLIC = settingsRepository.findByCodeIs(GlobalSettings.Code.STATISTICS_IS_PUBLIC);

            settings.setMultiuserMode(MULTIUSER_MODE.getValue().getValue());
            settings.setPostPremoderation(POST_PREMODERATION.getValue().getValue());
            settings.setStatisticsIsPublic(STATISTICS_IS_PUBLIC.getValue().getValue());
        });

        return settings;
    }

    public SettingsDTO saveSettings(SettingsDTO settings) {
        saveOneSetting(GlobalSettings.Code.MULTIUSER_MODE, settings.isMultiuserMode());
        saveOneSetting(GlobalSettings.Code.POST_PREMODERATION, settings.isPostPremoderation());
        saveOneSetting(GlobalSettings.Code.STATISTICS_IS_PUBLIC, settings.isStatisticsIsPublic());

        return getSettings();
    }

    private Settings saveOneSetting(GlobalSettings.Code code, boolean valueToUpdate) {
        GlobalSettings.Value value = valueToUpdate ? GlobalSettings.Value.YES : GlobalSettings.Value.NO;
        final var option = settingsRepository.findByCodeIs(code);

        if (!value.equals(option.getValue())) {
            option.setValue(value);
            settingsRepository.save(option);
        }

        return option;
    }

    public boolean isStatsPublic() {
        return settingsRepository.findByCodeIs(
                GlobalSettings.Code.STATISTICS_IS_PUBLIC
        ).getValue().getValue();
    }
}
