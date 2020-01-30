package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kershov.blogapp.utils.JsonViews;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SettingsDTO {
    @JsonProperty("MULTIUSER_MODE")
    @JsonView(JsonViews.Name.class)
    private boolean multiuserMode;

    @JsonProperty("POST_PREMODERATION")
    @JsonView(JsonViews.Name.class)
    private boolean postPremoderation;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    @JsonView(JsonViews.Name.class)
    private boolean statisticsIsPublic;
}
