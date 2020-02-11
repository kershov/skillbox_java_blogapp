package ru.kershov.blogapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer {
    @Autowired
    private StorageProperties storageProperties;

    /**
     * Configures mapping between urls & static resources:
     *   http://site.tld/{upload_dir}/* >> file:{upload_dir}/
     *
     * Example: http://site.tld/upload/a/b/c/file.txt
     * will be loaded from: file:./upload/a/b/c/file.txt
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String uploadPath = storageProperties.getLocation();

        registry.addResourceHandler(String.format("/%s/**", uploadPath))
                .addResourceLocations(String.format("file:%s/", uploadPath))
                .setCacheControl(CacheControl.maxAge(Config.INT_IMAGES_MAX_CACHE_AGE, TimeUnit.HOURS).cachePublic());
    }
}
