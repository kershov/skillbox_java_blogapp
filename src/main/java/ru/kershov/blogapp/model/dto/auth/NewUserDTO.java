package ru.kershov.blogapp.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @AllArgsConstructor @ToString
public class NewUserDTO {
    @JsonProperty(value = "e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    private final String email;

    @JsonIgnore
    private final String name;  // TODO: To be fixed later...

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    private final String password;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private final String captcha;

    @JsonProperty(value = "captcha_secret")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private final String captchaSecret;

    public String getName() {
        // TODO: Stub. There's an error in frontend Vue.js app.
        //       It doesn't have 'name' field @ /login/registration.
        //       Add validation if this issue will be resolved.
        return (name == null || name.isEmpty()) ? email : name;
    }
}
