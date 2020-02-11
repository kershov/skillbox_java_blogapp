package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kershov.blogapp.services.StorageService;
import ru.kershov.blogapp.services.UserAuthService;

@RestController
public class ApiImageUploadController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private StorageService storageService;

    @PostMapping(value = "/api/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        if (!userAuthService.isAuthorized())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String pathToSavedFile = storageService.store(image);

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/{root}/{file_uri}")
                .buildAndExpand(storageService.getRootLocation(), pathToSavedFile);

        return ResponseEntity.ok(uri.toUriString());
    }
}
