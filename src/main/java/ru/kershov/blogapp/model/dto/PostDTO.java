package ru.kershov.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import org.jsoup.Jsoup;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.utils.JsonViews;
import ru.kershov.blogapp.utils.TimeAgo;

import java.time.Instant;

public class PostDTO {
    @Getter
    @JsonView(JsonViews.Id.class)
    private int id;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private String title;

    /**
     * Текст анонса поста без HTML-тэгов
     */
    @Getter
    @JsonView(JsonViews.IdName.class)
    private String announce;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private String time;

    @Getter
    @JsonView(JsonViews.FullMessage.class)
    private PostAuthorDTO user;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private int viewCount;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private int commentCount;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private long likeCount;

    @Getter
    @JsonView(JsonViews.IdName.class)
    private long dislikeCount;

    @Getter
    private Instant date;

    public PostDTO(Post post, long likeCount, long dislikeCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.announce = Jsoup.parse(post.getText()).text();
        this.time = TimeAgo.toDuration(post.getTime());
        this.user = new PostAuthorDTO(post.getAuthor().getId(), post.getAuthor().getName());
        this.viewCount = post.getViewCount();

        this.commentCount = post.getComments().size();
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;

        this.date = post.getTime();
    }
}
