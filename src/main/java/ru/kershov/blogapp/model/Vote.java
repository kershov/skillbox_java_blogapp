package ru.kershov.blogapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "votes")
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true, of = {"time", "value"})
@ToString(callSuper = true, of = {"value"})
public class Vote extends AbstractEntity {
    public Vote(@NotNull User user, @NotNull Post post) {
        this.user = user;
        this.post = post;
    }

    public Vote(@NotNull User user, @NotNull Post post, @NotNull Instant time) {
        this(user, post);
        this.time = time;
    }

    /** Тот, кто поставил лайк / дизлайк */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /** Пост, которому поставлен лайк / дизлайк */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name="post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    /** Дата и время лайка / дизлайка */
    @NotNull @Column(nullable = false)
    private Instant time = Instant.now();

    /** Лайк или дизлайк: 1 или -1 */
    @Column(nullable = false)
    private byte value;

    public void like() {
        this.value = 1;
    }

    public void dislike() {
        this.value = -1;
    }
}
