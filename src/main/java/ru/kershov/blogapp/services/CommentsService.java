package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.components.TelegramClient;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.repositories.CommentsRepository;
import ru.kershov.blogapp.utils.StringUtils;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private TelegramClient telegramClient;

    public int addComment(User user, Comment parentComment, Post post, String text) {
        Comment newComment = new Comment();

        newComment.setParentComment(parentComment);
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setText(text);

        Comment savedComment = commentsRepository.save(newComment);

        telegramClient.sendMessage(String.format(Config.STRING_TELEGRAM_COMMENT_ADDED,
                StringUtils.escapeString(user.getName()),
                StringUtils.escapeString(user.getEmail()),
                StringUtils.escapeString(post.getTitle()), post.getId(),
                StringUtils.escapeString(savedComment.getText())
        ));

        return savedComment.getId();
    }
}
