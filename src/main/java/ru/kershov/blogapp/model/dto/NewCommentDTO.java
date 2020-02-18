package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCommentDTO {
    @Nullable
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("post_id")
    @NotNull(message = Config.STRING_COMMENT_POST_ID_IS_MANDATORY)
    @Min(value = 1, message = Config.STRING_WRONG_POST_ID)
    private Integer postId;

    @NotBlank(message = Config.STRING_COMMENT_WRONG_TEXT)
    private String text;
}
