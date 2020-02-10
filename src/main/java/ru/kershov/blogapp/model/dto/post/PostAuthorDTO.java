package ru.kershov.blogapp.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.utils.JsonViews;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostAuthorDTO {
    @JsonView({JsonViews.Id.class})
    private int id;

    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String name;
}
