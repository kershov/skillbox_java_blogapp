package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Post;

@Repository
public interface PostsRepository extends CrudRepository<Post, Integer> {

}
