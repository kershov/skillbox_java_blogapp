package ru.kershov.blogapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.enums.PostMode;
import ru.kershov.blogapp.model.dto.FrontPagePostsDTO;
import ru.kershov.blogapp.model.dto.PostDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.utils.OffsetLimitPageable;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class PostsService {
    @Autowired
    private PostsRepository postsRepository;

    public FrontPagePostsDTO getPosts(int offset, int limit, String postMode) {
        final List<PostDTO> posts;
        final Instant now = Instant.now();
        final PostMode mode = PostMode.getByName(postMode);
        final OffsetLimitPageable pageable;

        // Kinda hack too...
        // TODO: Think on how this can be refactored...
        final long TOTAL_POSTS = postsRepository.findAllPosts(now).size();

        switch (mode) {
            /* сортировать по дате публикации, выводить сначала старые */
            case EARLY:
                pageable = new OffsetLimitPageable(offset, limit, Sort.by("time").ascending());
                posts = postsRepository.findAllPosts(now, pageable);
                break;

            /* сортировать по убыванию количества лайков */
            case BEST:
                pageable = new OffsetLimitPageable(offset, limit, Sort.by(Sort.Order.desc("like_count"), Sort.Order.desc("time")));
                posts = postsRepository.findAllPosts(now, pageable);
                break;

            /* сортировать по убыванию количества комментариев */
            case POPULAR:
                pageable = new OffsetLimitPageable(offset, limit, Sort.by("time").descending());
                posts = postsRepository.findAllPosts(now, pageable);

                // Dirty hack:
                // Don't know how to sort by number of items inside an entity collection
                //   Entity >> Post.comments >> comments == Set<Comment>
                // TODO: Think on how this can be refactored...
                posts.sort(PostsService::sortByCommentsCountAndDate);
                break;

            /* сортировать по дате публикации, выводить сначала новые */
            case RECENT:
            default:
                pageable = new OffsetLimitPageable(offset, limit, Sort.by("time").descending());
                posts = postsRepository.findAllPosts(now, pageable);
                break;
        }

        return new FrontPagePostsDTO(posts, TOTAL_POSTS);
    }

    private static int sortByCommentsCountAndDate(PostDTO p1, PostDTO p2) {
        int result = p2.getCommentCount() - p1.getCommentCount();
        if (result == 0) result = p2.getDate().compareTo(p1.getDate());
        return result;
    }
}
