package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.utils.JsonViews;

@Data @NoArgsConstructor
public class StatsDTO {
    @JsonView(JsonViews.Name.class)
    private long postsCount;

    @JsonView(JsonViews.Name.class)
    private long likesCount;

    @JsonView(JsonViews.Name.class)
    private long dislikesCount;

    @JsonView(JsonViews.Name.class)
    private long viewsCount;

    @JsonView(JsonViews.Name.class)
    private String firstPublication;
}
