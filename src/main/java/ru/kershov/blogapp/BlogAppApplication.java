package ru.kershov.blogapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kershov.blogapp.config.AppSettings;
import ru.kershov.blogapp.config.AppSettingsInit;
import ru.kershov.blogapp.repositories.SettingsRepository;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private AppSettings appSettings;

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AppSettingsInit.init(appSettings, settingsRepository);
    }
}
