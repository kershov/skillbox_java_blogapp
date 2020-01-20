package ru.kershov.blogapp.model;

import lombok.*;
import ru.kershov.blogapp.enums.ModerationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts", indexes = {
    @Index(name = "idx_posts_title", columnList="title"),
    @Index(name = "idx_posts_active_status_date", columnList="is_active, moderation_status, time, id"),
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class Post extends AbstractEntity {
    /**Скрыта или активна публикация: 0 или 1 */
    @NotNull @Column(name = "is_active", columnDefinition = "TINYINT(1) NOT NULL", nullable = false)
    private boolean isActive;

    /** Статус модерации, по умолчанию значение “NEW” */
    @Enumerated(EnumType.STRING)
    @NotNull @Column(name = "moderation_status",
        columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED') DEFAULT 'NEW'", nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    /** ID пользователя-модератора, принявшего решение */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id",
        foreignKey = @ForeignKey(name = "fk_posts_moderator_id"), updatable = false, insertable = false)
    private User moderator;

    /** Автор поста */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
        foreignKey = @ForeignKey(name = "fk_posts_author_id"), updatable = false, insertable = false)
    private User author;

    /** Дата и время публикации поста */
    @NotNull @Column(nullable = false)
    private Instant time;

    /** Заголовок поста */
    @NotBlank @Size(max=255)
    @Column(nullable = false)
    private String title;

    /** Текст поста */
    @NotBlank @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    /** Количество просмотров поста */
    @NotNull @Column(name = "view_count", nullable = false)
    private int viewCount;

    /**
     * Теги, которыми отмечен данный пост
     *
     * MERGE: If the parent entity is merged into the persistence context,
     *        the related entity will also be merged.
     *
     * PERSIST: If the parent entity is persisted into the persistence context,
     *          the related entity will also be persisted.
     */
    @NotNull
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "posts_tags",
        joinColumns = @JoinColumn(name = "post_id",
                referencedColumnName="id", foreignKey = @ForeignKey(name = "fk_posts_tags_post_id")),

        inverseJoinColumns = @JoinColumn(name = "tag_id",
                referencedColumnName="id", foreignKey = @ForeignKey(name = "fk_posts_tags_tag_id")))
    private final Set<Tag> tags = new HashSet<>();

    /** Лайки / дизлайки поста */
    @NotNull
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private final Set<Vote> votes = new HashSet<>();
}
