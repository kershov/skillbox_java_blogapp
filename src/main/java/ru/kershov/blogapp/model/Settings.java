package ru.kershov.blogapp.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.enums.GlobalSettings;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "global_settings", indexes = {
    @Index(name = "idx_global_settings_code", columnList="code", unique = true)
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class Settings extends AbstractEntity {
    /**
     * Системное имя настройки
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(
        nullable = false,
        columnDefinition = "ENUM('MULTIUSER_MODE', 'POST_PREMODERATION', 'STATISTICS_IS_PUBLIC')")
    private GlobalSettings.Code code;

    /**
     * Название настройки
     */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String name;

    /**
     * Значение настройки
     */
    @Enumerated(EnumType.STRING)
    @NotNull @Column(columnDefinition = "ENUM('YES', 'NO')", nullable = false)
    private GlobalSettings.Value value;
}
