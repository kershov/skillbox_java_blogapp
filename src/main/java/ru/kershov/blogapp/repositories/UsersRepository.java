package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {

}
