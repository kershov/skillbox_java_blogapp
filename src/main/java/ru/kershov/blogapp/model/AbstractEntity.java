package ru.kershov.blogapp.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.utils.JsonViews;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data @NoArgsConstructor(force = true) @EqualsAndHashCode(of = {"id"})
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({JsonViews.Id.class})
    private int id;
}
