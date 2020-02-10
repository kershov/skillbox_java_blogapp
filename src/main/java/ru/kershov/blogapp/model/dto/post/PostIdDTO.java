package ru.kershov.blogapp.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class PostIdDTO {
    /**
     * DTO for restoring user's password
     */
    @JsonProperty(value = "post_id")
    @Size(min=1)
    private String postId;
}
