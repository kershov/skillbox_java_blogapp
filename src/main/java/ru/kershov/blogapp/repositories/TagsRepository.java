package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Tag;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {

}
