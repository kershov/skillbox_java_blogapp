package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.enums.GlobalSettings;
import ru.kershov.blogapp.model.Settings;

@Repository
public interface SettingsRepository extends CrudRepository<Settings, Integer> {
    Settings findByCodeIs(GlobalSettings.Code code);
}
