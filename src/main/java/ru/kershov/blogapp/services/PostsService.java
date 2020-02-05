package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.config.Config;
import ru.kershov.blogapp.enums.PostMode;
import ru.kershov.blogapp.exceptions.ResponseHandler;
import ru.kershov.blogapp.model.Comment;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.model.dto.FrontPagePostsDTO;
import ru.kershov.blogapp.model.dto.PostDTO;
import ru.kershov.blogapp.repositories.CommentsRepository;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.TagsRepository;
import ru.kershov.blogapp.repositories.VotesRepository;
import ru.kershov.blogapp.utils.DateUtils;

import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private CommentsRepository commentsRepository;

    public ResponseEntity<?> getPosts(int offset, int limit, String postMode) {
        final Instant now = Instant.now();
        final PostMode mode;

        Sort sort = Sort.by(Sort.Direction.DESC, "time");

        try {
            mode = PostMode.getByName(postMode);
        } catch (IllegalArgumentException e) {
            return new ResponseHandler().init(e.getMessage()).setStatus(HttpStatus.BAD_REQUEST)
                    .getResponse();
        }

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

        Pageable pageable = PageRequest.of(offset, limit, sort);
        Page<PostDTO> posts = postsRepository.findAllPosts(now, pageable);

        if (mode == PostMode.POPULAR) {
            final List<PostDTO> p = new ArrayList<>(posts.getContent());
            Collections.sort(p);
            posts = new PageImpl<>(p);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new FrontPagePostsDTO(posts.getContent(), posts.getTotalElements()));
    }

    public ResponseEntity<?> searchPosts(int offset, int limit, String query) {
        if (query == null || query.length() < Config.INT_POST_MIN_QUERY_LENGTH) {
            return new ResponseHandler().init(Config.STRING_POST_INVALID_QUERY)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .getResponse();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        Page<PostDTO> posts = postsRepository.findAllPostsByQuery(Instant.now(), query, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
                new FrontPagePostsDTO(posts.getContent(), posts.getTotalElements()));
    }

    public ResponseEntity<?> getPost(int id) {
        Post post = postsRepository.findPostById(id, Instant.now());

        if (post == null) {
            return new ResponseHandler().init(String.format(Config.STRING_POST_NOT_FOUND, id))
                    .setStatus(HttpStatus.NOT_FOUND)
                    .getResponse();
        }

        PostDTO postDTO = new PostDTO(post);
        postDTO.setLikeCount(votesRepository.findByPostAndValue(post, (byte) 1).size());
        postDTO.setDislikeCount(votesRepository.findByPostAndValue(post, (byte) -1).size());
        postDTO.setTags(tagsRepository.findTagNamesByPost(post));

        final List<Comment> comments = commentsRepository.findByPost(post);
        postDTO.setComments(comments);

        return ResponseEntity.status(HttpStatus.OK).body(postDTO);
    }

    public ResponseEntity<?> searchByDate(int offset, int limit, String date) {
        if (!DateUtils.isValidDate(date)) {
            return new ResponseHandler().init(Config.STRING_POST_INVALID_DATE)
                .setStatus(HttpStatus.BAD_REQUEST)
                .getResponse();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        Page<PostDTO> posts = postsRepository.findAllPostsByDate(Instant.now(), date, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
                new FrontPagePostsDTO(posts.getContent(), posts.getTotalElements()));
    }

    public ResponseEntity<?> searchByTag(int offset, int limit, String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);

        if (tag == null) {
            return new ResponseHandler().init(String.format(Config.STRING_POST_INVALID_TAG, tagName))
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .getResponse();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);

        Page<PostDTO> posts = postsRepository.findAllPostsByTag(Instant.now(), tag, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
                new FrontPagePostsDTO(posts.getContent(), posts.getTotalElements()));
    }
}
