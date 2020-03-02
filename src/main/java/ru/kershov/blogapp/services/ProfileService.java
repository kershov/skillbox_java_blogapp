package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.ProfileDTO;
import ru.kershov.blogapp.repositories.UsersRepository;
import ru.kershov.blogapp.utils.APIResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FileSystemStorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> updateUserProfile(User user, ProfileDTO profileData) {

        final Map<String, Object> errors = validateProfileFields(user, profileData);

        if (!errors.isEmpty())
            return ResponseEntity.ok(APIResponse.error(errors));

        final String photo = profileData.getPhoto();
        final boolean removePhoto = profileData.isRemovePhoto();
        final String name = profileData.getName();
        final String email = profileData.getEmail();
        final String password = profileData.getPassword();

        if (photo != null) {
            if (!photo.isBlank() && !photo.equals(user.getPhoto())) {
                user.setPhoto(photo);
            }
        }

        if (removePhoto) {
            if (user.getPhoto() != null) {
                storageService.delete(user.getPhoto());
                user.setPhoto(null);
            }
        }

        if (!name.isBlank() && !name.equals(user.getName())) {
            user.setName(name);
        }

        if (!email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }

        if (password != null) {
            if(!password.isBlank()) {
                user.setPassword(passwordEncoder.encode(password));
            }
        }

        User savedUser = usersRepository.save(user);

        return (user.getId() == savedUser.getId())
                ? ResponseEntity.ok(APIResponse.ok())
                : ResponseEntity.ok(APIResponse.error());
    }

    private Map<String, Object> validateProfileFields(User user, ProfileDTO profile) {
        final Pattern emailPattern = Pattern.compile(Config.STRING_EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        final Map<String, Object> errors = new HashMap<>();

        final String name = profile.getName();
        final String email = profile.getEmail();
        final String password = profile.getPassword();

        // name checks...
        if (name == null || name.isBlank() ||
                !(name.length() >= Config.INT_AUTH_MIN_NAME_LENGTH &&
                        name.length() <= Config.INT_AUTH_MAX_FIELD_LENGTH)) {
            errors.put("name", Config.STRING_AUTH_WRONG_NAME);
        }

        // email checks...
        if (email == null || email.isBlank() || !emailPattern.matcher(email).matches()) {
            errors.put("email", Config.STRING_AUTH_INVALID_EMAIL);
        } else if (!errors.containsKey("email") &&
                usersRepository.findByEmail(email) != null &&
                !user.getEmail().equals(email)
        ) {
            errors.put("email", Config.STRING_AUTH_EMAIL_ALREADY_REGISTERED);
        }

        // password checks...
        if (password != null) {
            if (!password.isBlank() &&
                    !(password.length() >= Config.INT_AUTH_MIN_PASSWORD_LENGTH &&
                    password.length() <= Config.INT_AUTH_MAX_FIELD_LENGTH
            )) {

                errors.put("password", Config.STRING_AUTH_INVALID_PASSWORD_LENGTH);
            }
        }

        return errors;
    }
}
