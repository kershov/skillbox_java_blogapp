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
import ru.kershov.blogapp.enums.ModerationDecision;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.ModerationDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.services.PostsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.APIResponse;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/moderation")
public class ApiModerationController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsService postsService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> moderate(@RequestBody @Valid ModerationDTO moderation) {
        Optional<User> userOptional = userAuthService.getAuthorizedUser();

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        User user = userOptional.get();

        if (!user.isModerator())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.error());

        final Optional<Post> postOptional = postsRepository.findById(moderation.getPostId());

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.error(String.format(Config.STRING_POST_NOT_FOUND, moderation.getPostId()))
            );
        }

        final Post post = postOptional.get();
        final ModerationDecision decision = ModerationDecision.valueOf(moderation.getDecision().toUpperCase());

        return postsService.updatePostModerationStatus(user, post, decision);
    }
}
