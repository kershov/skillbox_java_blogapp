package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.post.NewPostDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.TagsRepository;
import ru.kershov.blogapp.repositories.UsersRepository;
import ru.kershov.blogapp.services.PostsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.services.VotesService;
import ru.kershov.blogapp.utils.APIResponse;
import ru.kershov.blogapp.utils.ErrorValidation;
import ru.kershov.blogapp.utils.JsonViews;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsService postsService;

    @Autowired
    private VotesService votesService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "mode") String mode) {

        return postsService.getPosts(offset, limit, mode);
    }

    /**
     * Creates a new post by an authorized user.
     * All the validation handled by GlobalExceptionHandler.handleMethodArgumentNotValidException()
     * so there's no need to validate `newPost` fields.
     */
    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePost(@RequestBody @Valid NewPostDTO newPost) {
        User user = userAuthService.getAuthorizedUser();

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        // New post
        Post post = postsService.savePost(newPost, user);

        return ResponseEntity.ok((post != null) ? APIResponse.ok("id", post.getId()) : APIResponse.error());
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "query") String query) {

        return postsService.searchPosts(offset, limit, query);
    }

    @GetMapping(value = "/byDate", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByDate(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "date") String date) {

        return postsService.searchByDate(offset, limit, date);
    }

    @GetMapping(value = "/byTag", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> searchByTag(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "tag") String tagName) {

        return postsService.searchByTag(offset, limit, tagName);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.EntityIdName.class)
    public ResponseEntity<?> searchPosts(@PathVariable int id) {
        return postsService.getPost(id);
    }

    @PostMapping(value = "/{voteType:(?:dis)?like}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> vote(@PathVariable(value = "voteType") String voteType,
                                  @RequestBody Map<String, Integer> payload) {

        User user = userAuthService.getAuthorizedUser();
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        Integer postId = payload.getOrDefault("post_id", 0);
        if (postId <= 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());

        Post post = postsRepository.findById(postId).orElse(null);

        return votesService.vote(voteType, user, post);
    }
}
