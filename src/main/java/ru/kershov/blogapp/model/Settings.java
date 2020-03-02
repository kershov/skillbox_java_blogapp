package ru.kershov.blogapp.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ru.kershov.blogapp.enums.GlobalSettings;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "global_settings")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true)
public class Settings extends AbstractEntity {
    /** Системное имя настройки */
    @NaturalId
    @NotNull @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private GlobalSettings.Code code;

    /** Название настройки */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String name;

    /** Значение настройки */
    @NotNull @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private GlobalSettings.Value value;
}
