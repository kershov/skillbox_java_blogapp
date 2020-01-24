package ru.kershov.blogapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kershov.blogapp.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BlogAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);
    }
}
