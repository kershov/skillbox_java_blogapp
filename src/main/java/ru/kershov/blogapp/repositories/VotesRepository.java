package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.Vote;

@Repository
public interface VotesRepository extends CrudRepository<Vote, Integer> {

}
