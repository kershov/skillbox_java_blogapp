package ru.kershov.blogapp.model.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.kershov.blogapp.config.Config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class NewPostDTO {
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = Config.STRING_POST_TITLE_MIN_LENGTH, max = Config.STRING_POST_TITLE_MAX_LENGTH,
            message = Config.STRING_POST_INVALID_TITLE)
    private String title;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = Config.STRING_POST_TEXT_MIN_LENGTH, max = Config.STRING_POST_TEXT_MAX_LENGTH,
            message = Config.STRING_POST_INVALID_TEXT)
    private String text;

    @NotBlank
    private Boolean active;

    @NotNull
    private Instant time;

    @Nullable
    private Set<String> tags;
}
