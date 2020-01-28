package ru.kershov.blogapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.dto.PostDTO;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {
    /**
     * Должны выводиться только активные (поле “is_active” в таблице “posts” равно 1,
     * утверждённые модератором (поле “moderation_status” равно “ACCEPTED”) посты с
     * датой публикации не позднее текущего момента (движок должен позволять
     * откладывать публикацию)
     */

    String QUERY = "SELECT" +
            "    new ru.kershov.blogapp.model.dto.PostDTO(" +
            "        post, " +
            "        SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END) as like_count, " +
            "        SUM(CASE WHEN v.value = -1 THEN 1 ELSE 0 END)" +
            "    ) " +
            "FROM Post post " +
            "LEFT JOIN post.votes as v " +
            "WHERE post.isActive = 1 AND post.moderationStatus = 'ACCEPTED' AND post.time <= :date " +
            "GROUP BY 1";

    @Query(QUERY)
    List<PostDTO> findAllPosts(@Param("date") Instant date, Pageable pageable);

    @Query(QUERY)
    List<PostDTO> findAllPosts(@Param("date") Instant date);

    List<Post> findByModerationStatus(ModerationStatus moderationStatus);
}
