package ru.kershov.blogapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();

    String store(MultipartFile file);

    Path load(String filename);

    boolean delete(String filename);

    void deleteAll();

    Path getRootLocation();
}
