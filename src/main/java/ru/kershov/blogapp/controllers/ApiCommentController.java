package ru.kershov.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.NewCommentDTO;
import ru.kershov.blogapp.services.CommentsService;
import ru.kershov.blogapp.services.UserAuthService;
import ru.kershov.blogapp.utils.APIResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private CommentsService commentsService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(@RequestBody @Valid NewCommentDTO comment,
                                               Errors errors) {

        User user = userAuthService.getAuthorizedUser();

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error());

        int newCommentId = commentsService.addComment(comment, user);

        return ResponseEntity.ok(APIResponse.ok("id", newCommentId));
    }
}
