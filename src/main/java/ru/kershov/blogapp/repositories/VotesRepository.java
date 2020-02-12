package ru.kershov.blogapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.Vote;

import java.util.List;

@Repository
public interface VotesRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT COUNT(*) FROM Vote v WHERE (:user IS NULL OR v.user = :user) AND v.value = :value")
    int countByUserAndValue(@Param("user") User user, @Param("value") byte value);

    List<Vote> findByPostAndValue(Post post, byte value);

    Vote findByUserAndPost(User user, Post post);
}
