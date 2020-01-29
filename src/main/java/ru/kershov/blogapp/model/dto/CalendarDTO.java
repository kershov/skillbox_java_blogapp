package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kershov.blogapp.utils.JsonViews;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CalendarDTO {
    @JsonView(JsonViews.IdName.class)
    private final List<Integer> years;

    @JsonView(JsonViews.IdName.class)
    private final Map<String, Long> posts;
}
