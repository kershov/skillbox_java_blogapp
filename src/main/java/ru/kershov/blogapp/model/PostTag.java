package ru.kershov.blogapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "posts_tags", indexes = {
        @Index(name = "fk_posts_tags_post_id", columnList="post_id"),
        @Index(name = "fk_posts_tags_tag_id", columnList="tag_id"),
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostTag extends AbstractEntity {
    /**
     * Промежуточная таблица (Many-to-Many) между Постами и Тегами
     * Релизована через ManyToOne <> OneToMany, т.к. согласно условию нужен @Id
     */
    @NotNull
    @ManyToOne
    @JoinColumn(
        name="post_id", foreignKey = @ForeignKey(name = "fk_posts_tags_post_id"),
        referencedColumnName = "id", updatable = false, insertable = false)
    private Post post;

    @NotNull
    @ManyToOne
    @JoinColumn(
        name="tag_id", foreignKey = @ForeignKey(name = "fk_posts_tags_tag_id"),
        referencedColumnName = "id", updatable = false, insertable = false)
    private Tag tag;
}
