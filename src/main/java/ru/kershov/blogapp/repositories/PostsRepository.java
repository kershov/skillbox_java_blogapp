package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.model.Post;

import java.util.List;

@Repository
public interface PostsRepository extends CrudRepository<Post, Integer> {
    List<Post> findByModerationStatus(ModerationStatus moderationStatus);
}
