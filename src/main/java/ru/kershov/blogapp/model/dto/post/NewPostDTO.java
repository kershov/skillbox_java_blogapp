package ru.kershov.blogapp.model.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.kershov.blogapp.components.PostDateConverter;
import ru.kershov.blogapp.components.ValidPostDate;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class NewPostDTO {
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(message = Config.STRING_POST_INVALID_TITLE,
            min = Config.STRING_POST_TITLE_MIN_LENGTH, max = Config.STRING_POST_TITLE_MAX_LENGTH)
    private String title;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(message = Config.STRING_POST_INVALID_TEXT,
            min = Config.STRING_POST_TEXT_MIN_LENGTH, max = Config.STRING_POST_TEXT_MAX_LENGTH)
    private String text;

    @NotNull(message = Config.STRING_FIELD_CANT_BE_BLANK)
    private Boolean active;

    @ValidPostDate
    @JsonSerialize(using = PostDateConverter.Serialize.class)
    @JsonDeserialize(using = PostDateConverter.Deserialize.class)
    private Instant time;

    @Nullable
    private Set<String> tags = new HashSet<>();
}
