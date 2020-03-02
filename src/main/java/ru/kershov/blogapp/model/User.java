package ru.kershov.blogapp.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;
import ru.kershov.blogapp.utils.JsonViews;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true, of = {"email"})
@ToString(callSuper = true, of = {"name"})
public class User extends AbstractEntity {
    /**
     * Является ли пользователь модератором (может ли править
     * глобальные настройки сайта и модерировать посты)
     */
    @Column(name = "is_moderator", nullable = false)
    private boolean isModerator;

    /** Дата и время регистрации пользователя */
    @NotNull @Column(name = "reg_time", nullable = false)
    private Instant regTime;

    /** Имя пользователя */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String name;

    /** E-mail пользователя */
    @NaturalId(mutable = true) @Email @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String email;

    /** Хэш пароля пользователя */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String password;

    /** Код для восстановления пароля, может быть NULL */
    @Size(max=255)
    private String code;

    /** Фотография (ссылка на файл), может быть NULL */
    @Column(columnDefinition = "TEXT")
    @JsonView(JsonViews.EntityIdName.class)
    private String photo;

    /** Публикации пользователя */
    @NotNull
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Post> posts = new HashSet<>();

    /** Публикации, модерируемые пользователем */
    @NotNull
    @OneToMany(mappedBy = "moderatedBy", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<Post> moderatedPosts = new HashSet<>();

    /** Комментарии пользователя */
    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Comment> comments = new HashSet<>();

    /** Лайки / дизлайки пользователя */
    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Vote> votes = new HashSet<>();
}
