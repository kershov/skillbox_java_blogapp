package ru.kershov.blogapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.kershov.blogapp.enums.ModerationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true, of = {"title", "time"})
@ToString(callSuper = true, of = {"title"})
public class Post extends AbstractEntity {
    /** Скрыта или активна публикация: 0 или 1 */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** Статус модерации, по умолчанию значение “NEW” */
    @Enumerated(EnumType.STRING)
    @NotNull @Column(name = "moderation_status", length = 10, nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    /** ID пользователя-модератора, принявшего решение, или NULL */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "moderator_id", referencedColumnName="id")
    private User moderatedBy;

    /** Автор поста */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName="id")
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
    @Column(name = "view_count", nullable = false)
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "posts_tags",
        joinColumns = @JoinColumn(name = "post_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName="id"))
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<Tag> tags = new HashSet<>();

    /** Лайки / дизлайки поста */
    @NotNull
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private final Set<Vote> votes = new HashSet<>();

    /** Комментарии поста */
    @NotNull
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA) // Allows getting collection's size eagerly
    private final Set<Comment> comments = new HashSet<>();

    public void addTag(@NotNull Tag tag) {
        tags.add(tag);
    }

    public void updateViewCount() {
        this.viewCount++;
    }
}
