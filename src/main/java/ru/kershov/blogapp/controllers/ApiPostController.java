package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.utils.JsonViews;
import ru.kershov.blogapp.model.dto.FrontPagePostsDTO;
import ru.kershov.blogapp.services.PostsService;

import javax.persistence.EntityManager;

@Slf4j
@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    EntityManager em;

    @Autowired
    PostsService postsService;

    @GetMapping(value="", produces = "application/json")
    @JsonView(JsonViews.FullMessage.class)
    public ResponseEntity<FrontPagePostsDTO> getPosts(
            @RequestParam(name="offset") int offset,
            @RequestParam(name="limit") int limit,
            @RequestParam(name="mode") String mode) {

        FrontPagePostsDTO posts = postsService.getPosts(offset, limit, mode);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
