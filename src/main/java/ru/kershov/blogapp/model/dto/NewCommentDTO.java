package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class NewCommentDTO {
    @Nullable
    @JsonProperty("parent_id")
    private Integer parentId;

    @NotNull @Min(1)
    @JsonProperty("post_id")
    private Integer postId;

    @NotBlank
    private String text;
}
