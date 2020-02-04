package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.enums.PostMode;
import ru.kershov.blogapp.exceptions.ErrorHandler;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.dto.FrontPagePostsDTO;
import ru.kershov.blogapp.model.dto.PostDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.TagsRepository;
import ru.kershov.blogapp.repositories.VotesRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class PostsService {
    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private TagsRepository tagsRepository;

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {
        final List<PostDTO> posts;
        final Instant now = Instant.now();
        final PostMode mode;

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);

        try {
            mode = PostMode.getByName(postMode);
        } catch (IllegalArgumentException e) {
            return new ErrorHandler().init(e.getMessage()).setStatus(HttpStatus.BAD_REQUEST)
                    .getErrorResponse();
        }

        // Kinda hack too...
        // TODO: Think on how this can be refactored...
        final long TOTAL_POSTS = postsRepository
                .findDistinctByIsActiveAndModerationStatusAndTimeBefore(
                        true, ModerationStatus.ACCEPTED, now, pageable).size();

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                sort = Sort.by(Sort.Direction.ASC, "time");
                break;

            /* сортировать по убыванию количества лайков */
            case BEST:
                sort = Sort.by(Sort.Direction.DESC, "like_count");
                break;

            /* сортировать по убыванию количества комментариев */
            case POPULAR:
            /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                break;
        }

        pageable = PageRequest.of(offset, limit, sort);
        posts = postsRepository.findAllPosts(now, pageable);

        if (mode == PostMode.POPULAR) Collections.sort(posts);

        return ResponseEntity.status(HttpStatus.OK).body(new FrontPagePostsDTO(posts, TOTAL_POSTS));
    }

    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    public ResponseEntity<?> getPost(int id) {
        Post post = postsRepository.findPostById(id, Instant.now());

        if (post == null) {
            return new ErrorHandler().init(String.format(Config.STRING_POST_NOT_FOUND, id))
                    .setStatus(HttpStatus.NOT_FOUND)
                    .getErrorResponse();
        }

        PostDTO postDTO = new PostDTO(post);
        postDTO.setLikeCount(votesRepository.findByPostAndValue(post, (byte) 1).size());
        postDTO.setDislikeCount(votesRepository.findByPostAndValue(post, (byte) -1).size());
        postDTO.setTags(tagsRepository.findTagNamesByPostId(post.getId()));

        return ResponseEntity.status(HttpStatus.OK).body(postDTO);
    }

    public ResponseEntity<?> searchByDate(int offset, int limit, String date) {
        return null;
    }

    public ResponseEntity<?> searchByTag(int offset, int limit, String tag) {
        return null;
    }
}
