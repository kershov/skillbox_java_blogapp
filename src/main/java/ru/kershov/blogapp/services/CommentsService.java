package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.repositories.CommentsRepository;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public int addComment(User user, Comment parentComment, Post post, String text) {
        Comment newComment = new Comment();

        newComment.setParentComment(parentComment);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(text);

        Comment savedComment = commentsRepository.save(newComment);

        return savedComment.getId();
    }
}
