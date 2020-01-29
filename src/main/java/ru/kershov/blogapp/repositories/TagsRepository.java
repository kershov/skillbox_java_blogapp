package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Tag;

import java.util.List;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {
    List<Tag> findAll();
    List<Tag> findByNameContaining(String name);
}
