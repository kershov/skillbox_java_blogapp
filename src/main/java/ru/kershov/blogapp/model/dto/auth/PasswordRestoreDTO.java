package ru.kershov.blogapp.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @AllArgsConstructor @ToString
public class PasswordRestoreDTO {
        @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
        private final String code;

        @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
        @Size(min = Config.INT_AUTH_MIN_PASSWORD_LENGTH,
              max = Config.INT_AUTH_MAX_FIELD_LENGTH,
              message = Config.STRING_AUTH_SHORT_PASSWORD)
        private final String password;

        @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
        private final String captcha;

        @JsonProperty(value = "captcha_secret")
        @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
        private final String captchaSecret;
}
