package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.NewCommentDTO;
import ru.kershov.blogapp.repositories.CommentsRepository;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.services.CommentsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.APIResponse;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(@Valid @RequestBody NewCommentDTO comment) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        Optional<Post> post = postsRepository.findById(comment.getPostId());
        Optional<Comment> parentComment = Optional.empty();

        if (post.isEmpty())
            return ResponseEntity.badRequest().body(
                    APIResponse.error(Config.STRING_WRONG_POST_ID, new HashMap<>() {{
                        put("post_id", Config.STRING_WRONG_POST_ID);
                    }})
            );

        if (comment.getParentId() != null) {
            Set<Comment> postComments = post.get().getComments();
            parentComment = commentsRepository.findById(comment.getParentId());

            if (parentComment.isEmpty() || (!postComments.isEmpty() && !postComments.contains(parentComment.get()))) {
                return ResponseEntity.badRequest().body(
                            APIResponse.error(Config.STRING_COMMENT_WRONG_PARENT_ID, new HashMap<>() {{
                                put("parent_id", Config.STRING_COMMENT_WRONG_PARENT_ID);
                            }})
                    );
            }
        }

        int newCommentId = commentsService.addComment(
                userOptional.get(), parentComment.orElse(null), post.get(), comment.getText()
        );

        return ResponseEntity.ok(APIResponse.ok("id", newCommentId));
    }
}
