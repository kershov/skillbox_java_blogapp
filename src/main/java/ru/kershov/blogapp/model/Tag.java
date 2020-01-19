package ru.kershov.blogapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_tags_name", columnList="name", unique = true),
})
@Data
@NoArgsConstructor(force = true) @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity {
    /**
     * Имя тега
     */
    @NotBlank
    @Column(nullable = false)
    private String name;

    // TODO: Backref to Posts
}
