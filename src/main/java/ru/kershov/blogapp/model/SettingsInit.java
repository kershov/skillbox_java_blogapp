package ru.kershov.blogapp.model;

import ru.kershov.blogapp.enums.GlobalSettings;
import ru.kershov.blogapp.model.dao.SettingsDAO;

import static ru.kershov.blogapp.enums.GlobalSettings.Code.*;

public class SettingsInit {

    public static void init(SettingsDAO settingsDAO) {
        Settings multiUserMode = new Settings();
        multiUserMode.setCode(MULTIUSER_MODE);
        multiUserMode.setName(MULTIUSER_MODE.getName());
        multiUserMode.setValue(GlobalSettings.Value.NO);

        Settings postPremoderation = new Settings();
        postPremoderation.setCode(POST_PREMODERATION);
        postPremoderation.setName(POST_PREMODERATION.getName());
        postPremoderation.setValue(GlobalSettings.Value.YES);

        Settings statIsPublic = new Settings();
        statIsPublic.setCode(STATISTICS_IS_PUBLIC);
        statIsPublic.setName(STATISTICS_IS_PUBLIC.getName());
        statIsPublic.setValue(GlobalSettings.Value.YES);

        settingsDAO.save(multiUserMode);
        settingsDAO.save(postPremoderation);
        settingsDAO.save(statIsPublic);
    }
}
