package ru.kershov.blogapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "captcha_codes")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true)
public class CaptchaCode extends AbstractEntity {
    /** Дата и время генерации кода капчи */
    @NotNull @Column(nullable = false)
    private Instant time;

    /** Код, отображаемый на картинкке капчи */
    @NotBlank @Column(nullable = false)
    private String code;

    /** Код, передаваемый в параметре */
    @NotBlank @Column(name = "secret_code", nullable = false)
    private String secretCode;
}
