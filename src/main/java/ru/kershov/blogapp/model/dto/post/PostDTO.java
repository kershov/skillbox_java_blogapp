package ru.kershov.blogapp.model.dto.post;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.utils.JsonViews;
import ru.kershov.blogapp.utils.TimeAgo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostDTO implements Comparable<PostDTO> {
    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityId.class})
    private int id;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String title;

    /**
     * Текст анонса поста без HTML-тэгов
     */
    @Getter
    @JsonView(JsonViews.IdName.class)
    private String announce;

    @Getter
    @JsonView(JsonViews.EntityIdName.class)
    private String text;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private String time;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private PostAuthorDTO user;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private int viewCount;

    @Getter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private int commentCount;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long likeCount;

    @Getter @Setter
    @JsonView({JsonViews.IdName.class, JsonViews.EntityIdName.class})
    private long dislikeCount;

    @Getter @Setter
    @JsonView(JsonViews.EntityIdName.class)
    private List<String> tags;

    @Getter @Setter
    @JsonView(JsonViews.EntityIdName.class)
    private List<Comment> comments;

    @Getter
    private Instant date;

    public PostDTO(Post post) {
        this(post, 0, 0);
    }

    public PostDTO(Post post, long likeCount, long dislikeCount) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.announce = Jsoup.parse(post.getText()).text();
        this.time = TimeAgo.toDuration(post.getTime());
        this.user = new PostAuthorDTO(post.getAuthor().getId(), post.getAuthor().getName());
        this.viewCount = post.getViewCount();

        this.commentCount = post.getComments().size();
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;

        this.date = post.getTime();
        this.comments = new ArrayList<>();
    }

    @Override
    public int compareTo(PostDTO o) {
        int result = o.getCommentCount() - this.getCommentCount();
        if (result == 0) result = o.getDate().compareTo(this.getDate());
        return result;
    }
}
