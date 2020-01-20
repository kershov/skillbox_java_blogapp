package ru.kershov.blogapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "votes")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Vote extends AbstractEntity {
    /** Тот, кто поставил лайк / дизлайк */
    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_votes_user_id"),
        updatable = false, insertable = false, nullable = false)
    private User user;

    /** Пост, которому поставлен лайк / дизлайк */
    @NotNull
    @ManyToOne
    @JoinColumn(name="post_id", referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_votes_post_id"),
        updatable = false, insertable = false, nullable = false)
    private Post post;

    /** Дата и время лайка / дизлайка */
    @NotNull @Column(nullable = false)
    private Instant time;

    /** Лайк или дизлайк: 1 или -1 */
    @NotNull @Min(-1) @Max(1) @Column(nullable = false, columnDefinition = "TINYINT(1) NOT NULL")
    private int value;
}
