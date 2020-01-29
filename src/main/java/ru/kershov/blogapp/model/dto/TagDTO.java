package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.ToString;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.utils.JsonViews;

@ToString(of = {"name", "baseWeight", "weight", "totalPostsWithTag"})
public class TagDTO {
    @Getter
    @JsonView(JsonViews.IdName.class)
    private final String name;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private double weight;

    @Getter
    private double baseWeight;

    final long totalPostsWithTag;

    public TagDTO(Tag tag, long totalPosts) {
        this.name = tag.getName();
        this.totalPostsWithTag = tag.getPosts().size();
        this.baseWeight = totalPostsWithTag / (double) totalPosts;
        this.weight = 0.0;
    }

    public void setWeight(double maxWeight) {
        this.weight = baseWeight / maxWeight;
    }
}
