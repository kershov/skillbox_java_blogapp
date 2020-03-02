package ru.kershov.blogapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kershov.blogapp.utils.JsonViews;
import ru.kershov.blogapp.utils.TimeAgo;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true, of = {"text", "time"})
@ToString(callSuper = true, of = {"text", "user", "time"})
public class Comment extends AbstractEntity  {
    /**
     * Комментарий, на который оставлен этот комментарий (может быть NULL,
     * если комментарий оставлен просто к посту)
     */
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName = "id")
    private Comment parentComment;

    @NotNull
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Comment> childComments = new HashSet<>();

    /** Автор комментария */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonView(JsonViews.EntityIdName.class)
    private User user;

    /** Пост, к которому написан комментарий */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name="post_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Post post;

    /** Дата и время комментария */
    @NotNull
    @Column(nullable = false)
    @JsonProperty("raw_time")
    private Instant time = Instant.now();

    /** Текст комментария */
    @NotBlank @Column(columnDefinition = "TEXT", nullable = false)
    @JsonView(JsonViews.EntityIdName.class)
    private String text;

    @Transient
    @JsonProperty("time")
    @JsonView(JsonViews.EntityIdName.class)
    private String timeAgoTime;

    public String getTimeAgoTime() {
        return TimeAgo.toDuration(getTime());
    }
}
