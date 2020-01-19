package ru.kershov.blogapp.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Settings;

@Repository
public interface SettingsDAO extends CrudRepository<Settings, Long> {

}
