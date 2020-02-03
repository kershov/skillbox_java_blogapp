package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.utils.JsonViews;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostAuthorDTO {
    @JsonView({JsonViews.Id.class, JsonViews.Entity.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.Entity.class})
    private String name;
}
