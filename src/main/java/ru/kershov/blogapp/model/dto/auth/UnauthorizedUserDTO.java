package ru.kershov.blogapp.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @AllArgsConstructor
@ToString
public class UnauthorizedUserDTO {
    /**
     * DTO for a user to be authorized
     */
    @JsonProperty(value = "e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private final String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private final String password;
}
