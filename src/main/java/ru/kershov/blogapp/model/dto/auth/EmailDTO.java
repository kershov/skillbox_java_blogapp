package ru.kershov.blogapp.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class EmailDTO {
    /**
     * DTO for restoring user's password
     */
    @JsonProperty(value = "email")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private String email;
}