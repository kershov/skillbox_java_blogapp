package ru.kershov.blogapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kershov.blogapp.utils.CaptchaUtils;
import ru.kershov.blogapp.utils.JsonViews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "captcha_codes")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true)
@ToString(of = {"code", "secretCode", "imageBase64"})
public class CaptchaCode extends AbstractEntity {
    /** Дата и время генерации кода капчи */
    @NotNull @Column(nullable = false)
    private Instant time;

    /** Код, отображаемый на картинкке капчи */
    @NotBlank @Column(nullable = false)
    private String code;

    /** Код, передаваемый в параметре */
    @NotBlank @Column(name = "secret_code", nullable = false)
    @JsonProperty("secret")
    @JsonView(JsonViews.Name.class)
    private String secretCode;

    @Transient
    @JsonProperty("image")
    @JsonView(JsonViews.Name.class)
    private String imageBase64;

    public CaptchaCode(int codeLength, int fontSize) {
        setTime(Instant.now());
        setCode(CaptchaUtils.getRandomCode(codeLength));
        setSecretCode(UUID.randomUUID().toString());
        setImageBase64(CaptchaUtils.getImageBase64(getCode(), fontSize));
    }

    public boolean isValidCode(String userCaptchaCode) {
        return code.equals(userCaptchaCode);
    }
}
