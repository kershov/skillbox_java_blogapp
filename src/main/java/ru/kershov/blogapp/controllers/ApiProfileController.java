package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.ProfileDTO;
import ru.kershov.blogapp.services.ProfileService;
import ru.kershov.blogapp.services.StorageService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.APIResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile/my")
public class ApiProfileController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private StorageService storageService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                    @RequestParam("removePhoto") boolean removePhoto,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("password") String password) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        String pathToSavedFile = storageService.store(photo);

        UriComponents photoUri = UriComponentsBuilder.newInstance()
                .path("{root}/{file_uri}")
                .buildAndExpand(storageService.getRootLocation(), pathToSavedFile);

        ProfileDTO profileData = ProfileDTO.builder()
                .photo(photoUri.toUriString())
                .removePhoto(removePhoto)
                .name(name)
                .email(email)
                .password(password)
                .build();

        return profileService.updateUserProfile(user, profileData);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileData) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        return profileService.updateUserProfile(user, profileData);
    }
}
