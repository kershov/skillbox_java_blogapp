package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.enums.MyPostsModerationStatus;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.post.NewPostDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.services.PostsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.services.VotesService;
import ru.kershov.blogapp.utils.APIResponse;
import ru.kershov.blogapp.utils.JsonViews;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PostsRepository postsRepository;

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

    @GetMapping(value = "/moderation", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getModeratedPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") ModerationStatus status) {

        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        if (!user.isModerator())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.error());

        // If param isn't set, initialize it as NEW
        status = (status == null) ? ModerationStatus.NEW : status;

        return postsService.getModeratedPosts(offset, limit, user, status);
    }

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.IdName.class)
    public ResponseEntity<?> getMyPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") MyPostsModerationStatus status) {

        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        // If param isn't set, initialize it as INACTIVE
        status = (status == null) ? MyPostsModerationStatus.INACTIVE : status;


        return postsService.getMyPosts(offset, limit, userOptional.get(), status);
    }

    @PostMapping(value = "/{voteType:(?:dis)?like}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> vote(@PathVariable(value = "voteType") String voteType,
                                  @RequestBody Map<String, Integer> payload) {

        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        Integer postId = payload.getOrDefault("post_id", 0);

        if (postId <= 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());

        Post post = postsRepository.findById(postId).orElse(null);

        return votesService.vote(voteType, userOptional.get(), post);
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
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        Post savedPost = postsService.savePost(null, newPost, user);

        postsService.notifyPostAdded(user, savedPost);

        return ResponseEntity.ok(APIResponse.ok("id", savedPost.getId()));
    }

    @PutMapping(value = "/{id:[1-9]\\d*}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPost(@PathVariable int id,
                                      @RequestBody @Valid NewPostDTO newPostData) {

        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();
        Optional<Post> postOptional = postsRepository.findById(id);

        if (postOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());

        Post post = postOptional.get();

        // Post can be edited by: its author, any moderator if moderator is not set or
        // moderator who is already moderating this post
        if (!post.getAuthor().equals(user) &&
                (!user.isModerator() || (post.getModeratedBy() != null &&
                        !post.getModeratedBy().equals(user)))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.error());
        }

        Post savedPost = postsService.savePost(post, newPostData, user);

        if (savedPost.getModerationStatus() == ModerationStatus.NEW) {
            postsService.notifyPostAdded(user, savedPost);
        }

        return ResponseEntity.ok(APIResponse.ok("id", savedPost.getId()));
    }
}
