package ru.kershov.blogapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.kershov.blogapp.config.AppSettings;
import ru.kershov.blogapp.config.AppSettingsInit;
import ru.kershov.blogapp.repositories.SettingsRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.kershov.blogapp"})
@EntityScan("ru.kershov.blogapp.model")
@EnableJpaRepositories("ru.kershov.blogapp.repositories")
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
        // TODO: Temporary. To be refactored. Check if settings persist in the DB >> Do nothing if so
        AppSettingsInit.init(appSettings, settingsRepository);
    }
}
