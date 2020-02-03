package ru.kershov.blogapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.Vote;

import java.util.List;

@Repository
public interface VotesRepository extends JpaRepository<Vote, Integer> {
    String QUERY = "SELECT COUNT(*) FROM Vote v WHERE v.value = :vote";

    @Query(QUERY)
    Long getVotes(@Param("vote") byte vote);

    List<Vote> findByPostAndValue(Post post, byte value);
}
