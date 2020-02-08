package ru.kershov.blogapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.User;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {
    List<User> findByIsModeratorTrue();
    List<User> findByIsModeratorFalse();
    User findByEmail(String email);
    User findByCode(String code);
}
