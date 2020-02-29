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

    @Getter
    private Tag tag;

    private final long totalPostsWithTag;

    public TagDTO(Tag tag, long totalPostsWithTag) {
        this.tag = tag;
        this.name = tag.getName();
        this.totalPostsWithTag = totalPostsWithTag;
        this.weight = 0.0;
    }

    public void setBaseWeight(long totalPosts) {
        this.baseWeight = totalPostsWithTag / (double) totalPosts;
    }

    public void setWeight(double maxWeight) {
        this.weight = baseWeight / maxWeight;
    }
}
