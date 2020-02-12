package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.NewCommentDTO;
import ru.kershov.blogapp.repositories.CommentsRepository;
import ru.kershov.blogapp.repositories.PostsRepository;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    public int addComment(NewCommentDTO comment, User user) {
        Integer parentId = comment.getParentId();
        Post post = postsRepository.findById(comment.getPostId()).orElse(null);

        Comment newComment = new Comment();

        newComment.setParentComment((parentId == null) ?
                null : commentsRepository.findById(parentId).orElse(null));
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(comment.getText());

        Comment savedComment = commentsRepository.save(newComment);

        return savedComment.getId();
    }
}
