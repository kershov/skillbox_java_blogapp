package ru.kershov.blogapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_tags_name", columnList="name", unique = true),
})
@Data
@NoArgsConstructor(force = true) @EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity {
    /** Имя тега */
    @NaturalId @NotBlank @Size(max = 255)
    @Column(nullable = false)
    private String name;

    /** Посты, отмеченные конкретным тегом */
    @NotNull
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private final Set<Post> posts = new HashSet<>();
}
