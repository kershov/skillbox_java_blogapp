package ru.kershov.blogapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity(name = "CaptchaCode") @Table(name = "captcha_codes")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor
public class CaptchaCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Дата и время генерации кода капчи
     */
    @NotNull @Column(nullable = false)
    private Instant time;

    /**
     * Код, отображаемый на картинкке капчи
     */
    @NotNull @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String code;

    /**
     * Код, передаваемый в параметре
     */
    @NotNull @Column(name = "secret_code", columnDefinition = "TINYTEXT", nullable = false)
    private String secretCode;
}
