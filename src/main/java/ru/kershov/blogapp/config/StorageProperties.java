package ru.kershov.blogapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("blogapp.upload-dir")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Getter @Setter
    private String location;
}
