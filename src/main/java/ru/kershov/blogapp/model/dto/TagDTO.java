package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import ru.kershov.blogapp.utils.JsonViews;

public class TagDTO {
    @Getter
    @JsonView(JsonViews.Id.class)
    private double weight;

    @Getter @Setter
    @JsonView(JsonViews.IdName.class)
    private String name;

    public TagDTO(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }
}
