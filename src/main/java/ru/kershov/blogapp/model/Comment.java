package ru.kershov.blogapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments", indexes = {
    @Index(name = "idx_comments_post_id", columnList="post_id"),
    @Index(name = "idx_comments_user_id", columnList="user_id"),
    @Index(name = "idx_comments_parent", columnList="parent_id"),
    @Index(name = "idx_comments_time", columnList="time"),
})
@Data @NoArgsConstructor(force = true) @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Comment extends AbstractEntity  {
    /**
     * Комментарий, на который оставлен этот комментарий (может быть NULL,
     * если комментарий оставлен просто к посту)
     */
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_comments_parent_id"),
        updatable = false, insertable = false)
    private Comment parentComment;

    @NotNull
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Comment> childComments = new HashSet<>();

    /** Автор комментария */
    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_comments_user_id"),
        updatable = false, insertable = false, nullable = false)
    private User user;

    /** Пост, к которому написан комментарий */
    @NotNull
    @ManyToOne
    @JoinColumn(name="post_id", referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_comments_post_id"),
        updatable = false, insertable = false, nullable = false)
    private Post post;

    /** Дата и время комментария */
    @NotNull
    @Column(nullable = false)
    private Instant time;
}
