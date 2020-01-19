package ru.kershov.blogapp.model;

import lombok.*;
import ru.kershov.blogapp.enums.ModerationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "posts", indexes = {
    @Index(name = "idx_posts_title", columnList="title"),
    @Index(name = "idx_posts_active_status_date", columnList="is_active, moderation_status, time, id"),
    @Index(name = "fk_posts_moderator", columnList="moderator_id"),
    @Index(name = "fk_posts_author", columnList="user_id"),
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class Post extends AbstractEntity {
    /**
     * Скрыта или активна публикация: 0 или 1
     */
    @NotNull @Column(name = "is_active", columnDefinition = "TINYINT(1) NOT NULL", nullable = false)
    private boolean isActive;

    /**
     * Статус модерации, по умолчанию значение “NEW”
     */
    @Enumerated(EnumType.STRING)
    @NotNull @Column(name = "moderation_status", columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED') DEFAULT 'NEW'", nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    /**
     * ID пользователя-модератора, принявшего решение
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", foreignKey = @ForeignKey(name = "fk_posts_moderator"))
    private User moderator;

    /**
     * Автор поста
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_posts_author"), updatable = false, insertable = false)
    private User author;

    /**
     * Дата и время публикации поста
     */
    @NotNull @Column(nullable = false)
    private Instant time;

    /**
     * Заголовок поста
     */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String title;

    /**
     * Текст поста
     */
    @NotBlank @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    /**
     * Количество просмотров поста
     */
    @NotNull @Column(name = "view_count", nullable = false)
    private int viewCount;

    // TODO: Comments, Tags, Votes
}
