package ru.kershov.blogapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Tag;

import java.util.List;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {
    List<Tag> findAll();
    List<Tag> findByNameContaining(String name);

    @Query("SELECT t.name FROM Tag t JOIN t.posts p WHERE p.id = :post_id")
    List<String> findTagNamesByPostId(@Param("post_id") int postId);
}
