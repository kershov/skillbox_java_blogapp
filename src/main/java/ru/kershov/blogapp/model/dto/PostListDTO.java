package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.kershov.blogapp.model.dto.post.PostDTO;
import ru.kershov.blogapp.utils.JsonViews;

import java.util.List;

public class PostListDTO {
    @Getter
    @JsonView(JsonViews.Id.class)
    private final long count;

    @Getter @Setter
    @JsonView(JsonViews.IdName.class)
    private List<PostDTO> posts;

    public PostListDTO(Page<PostDTO> posts) {
        this.posts = posts.getContent();
        this.count = posts.getTotalElements();
    }
}
