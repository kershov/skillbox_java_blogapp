package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.services.PostsService;
import ru.kershov.blogapp.utils.JsonViews;

import javax.persistence.EntityManager;
import java.time.Instant;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    EntityManager em;

    @Autowired
    PostsService postsService;

    @GetMapping(value="", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getPosts(
            @RequestParam(name="offset") int offset,
            @RequestParam(name="limit") int limit,
            @RequestParam(name="mode") String mode) {

        return postsService.getPosts(offset, limit, mode);
    }

    @GetMapping(value="/search", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchPosts(
            @RequestParam(name="offset") int offset,
            @RequestParam(name="limit") int limit,
            @RequestParam(name="query") String query) {

        return postsService.searchPosts(offset, limit, query);
    }

    @GetMapping(value="/byDate", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByDate(
            @RequestParam(name="offset") int offset,
            @RequestParam(name="limit") int limit,
            @RequestParam(name="date") String date) {

        return postsService.searchByDate(offset, limit, date);
    }

    @GetMapping(value="/byTag", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByTag(
            @RequestParam(name="offset") int offset,
            @RequestParam(name="limit") int limit,
            @RequestParam(name="tag") String tag) {

        return postsService.searchByTag(offset, limit, tag);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    @JsonView(JsonViews.Entity.class)
    public ResponseEntity<?> searchPosts(@PathVariable int id) {
        return postsService.getPost(id);
    }
}
