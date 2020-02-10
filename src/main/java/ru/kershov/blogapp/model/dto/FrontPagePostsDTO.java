package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import ru.kershov.blogapp.model.dto.post.PostDTO;
import ru.kershov.blogapp.utils.JsonViews;

import java.util.List;

public class FrontPagePostsDTO {
    @Getter
    @JsonView(JsonViews.Id.class)
    private final long count;

    @Getter @Setter
    @JsonView(JsonViews.IdName.class)
    private List<PostDTO> posts;

    public FrontPagePostsDTO(List<PostDTO> posts, long totalPosts) {
        this.posts = posts;
        this.count = totalPosts;
    }
}
