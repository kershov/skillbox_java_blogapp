package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Comment;

@Repository
public interface CommentsRepository extends CrudRepository<Comment, Integer> {

}
