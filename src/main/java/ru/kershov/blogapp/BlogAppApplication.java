package ru.kershov.blogapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kershov.blogapp.model.SettingsInit;
import ru.kershov.blogapp.model.dao.SettingsDAO;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {
    @Autowired
    SettingsDAO settingsDAO;

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SettingsInit.init(settingsDAO);
    }
}
