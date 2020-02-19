package ru.kershov.blogapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.enums.MyPostsModerationStatus;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ModerationStatus.StringToEnumConverter());
        registry.addConverter(new MyPostsModerationStatus.StringToEnumConverter());
    }
}
