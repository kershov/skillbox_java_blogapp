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

    String WHERE = "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :date ";
    String QUERY = "SELECT" +
            "    new ru.kershov.blogapp.model.dto.PostDTO(" +
            "        p, " +
            "        SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END) as like_count, " +
            "        SUM(CASE WHEN v.value = -1 THEN 1 ELSE 0 END)" +
            "    ) " +
            "FROM Post p " +
            "LEFT JOIN p.votes as v " +
            WHERE +
            "GROUP BY 1";

    @Query(QUERY)
    List<PostDTO> findAllPosts(@Param("date") Instant date, Pageable pageable);

    @Query(QUERY)
    List<PostDTO> findAllPosts(@Param("date") Instant date);

    List<Post> findByModerationStatus(ModerationStatus moderationStatus);

    @Query("SELECT SUM(p.viewCount) FROM Post p")
    Long getViews();

    @Query("SELECT DATE_FORMAT(p.time,'%Y-%m-%d %H:%m') as post_date " +
           "FROM Post p WHERE p.time = (SELECT MIN(p.time) FROM Post p)")
    String getFirstPostPublicationDate();

    List<Post> findDistinctByIsActiveAndModerationStatusAndTimeBefore(boolean isActive, ModerationStatus moderationStatus, Instant time, Pageable pageable);

    @Query("SELECT p FROM Post p " + WHERE + " AND p.id = :id")
    Post findPostById(@Param("id") int id, @Param("date") Instant date);
}
