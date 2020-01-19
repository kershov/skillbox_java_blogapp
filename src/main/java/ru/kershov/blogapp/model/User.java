package ru.kershov.blogapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList="email", unique = true),
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor
@ToString(exclude = {"posts"}) @EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity{
    /**
     * Является ли пользователь модератором (может ли править глобальные настройки сайта и модерировать посты)
     */
    @NotNull @Column(name = "is_moderator", columnDefinition = "TINYINT(1) NOT NULL", nullable = false)
    private boolean isModerator;

    /**
     * Дата и время регистрации пользователя
     */
    @NotNull @Column(name = "reg_time", nullable = false)
    private Instant regTime;

    /**
     * Имя пользователя
     */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String name;

    /**
     * E-mail пользователя
     */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String email;

    /**
     * Хэш пароля пользователя
     */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String password;

    /**
     * Код для восстановления пароля, может быть NULL
     */
    @Size(max=255)
    private String code;

    /**
     * Фотография (ссылка на файл), может быть NULL
     */
    @Column(columnDefinition = "TEXT")
    private String photo;

    /**
     * Публикации пользователя
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

//    @PrePersist
//    public void prePersist() { }
//
//    @PreUpdate
//    public void preUpdate() { }
}
