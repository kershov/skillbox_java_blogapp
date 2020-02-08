package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.services.TagsService;
import ru.kershov.blogapp.utils.JsonViews;

@RestController
@RequestMapping("/api/tag")
public class ApiTagController {
    @Autowired
    TagsService tagsService;

    @GetMapping(value="", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getTags(
            @RequestParam(name="query", required = false) String query) {
            return tagsService.getWeightedTags(query);
    }
}
