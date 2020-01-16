package ru.kershov.blogapp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "blogapp")
@Configuration("AppProperties")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AppProperties {
    private String title;
    private String subtitle;
    private String phone;
    private String email;
    private String copyright;
    private String copyrightFrom;
}
